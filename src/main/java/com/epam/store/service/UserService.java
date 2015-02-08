package com.epam.store.service;

import com.epam.store.PasswordEncryptor;
import com.epam.store.dao.*;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.model.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private DaoFactory daoFactory;
    private SqlQueryGenerator sqlQueryGenerator;

    public UserService(DaoFactory daoFactory, SqlQueryGenerator sqlQueryGenerator) {
        this.daoFactory = daoFactory;
        this.sqlQueryGenerator = sqlQueryGenerator;
    }

    public User findUser(String email) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            return userDao.findFirstByParameter("email", email);
        }
    }

    public User registerUser(String name, String email, String password) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            if (findUser(email) != null) throw new ServiceException("User with such email already registered");
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            Dao<Role> roleDao = daoSession.getDao(Role.class);
            Role userRole = roleDao.findFirstByParameter("name", Role.USER_ROLE_NAME);
            user.setRole(userRole);
            user.setPassword(PasswordEncryptor.encrypt(password));
            return userDao.insert(user);
        }
    }

    /**
     * Checks if in database exist user with such email and password.
     *
     * @param email    user's email
     * @param password user's password
     * @return User object from database, or null, if such user didn't found
     */
    public User authenticate(String email, String password) {
        User authenticatedUser = null;
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            User foundUser = findUser(email);
            if (foundUser != null && PasswordEncryptor.comparePassword(password, foundUser.getPassword())) {
                authenticatedUser = foundUser;
            }
        }
        return authenticatedUser;
    }


    public List<User> getAllUsers() {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            return userDao.findByParameter("ROLE_ID", Role.USER_ROLE_ID);
        }
    }

    public boolean setUserBan(long userId, boolean ban) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            User user = userDao.find(userId);
            if (user == null) return false;
            user.setBanned(ban);
            userDao.update(user);
            return true;
        }
    }

    public List<Order> getUserOrderList(long userID) {
        List<Purchase> purchaseList;
        List<Order> orderList = new ArrayList<>();
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Purchase> purchaseDao = daoSession.getDao(Purchase.class);
            purchaseList = purchaseDao.findByParameter("USER_ID", userID);
        }
        Set<Date> dates = new HashSet<>();
        for (Purchase purchase : purchaseList) {
            dates.add(purchase.getDate());
        }
        for (Date date : dates) {
            List<Purchase> orderPurchaseList = purchaseList.stream()
                    .filter(purchase -> purchase.getDate().equals(date))
                    .collect(Collectors.toList());
            orderList.add(new Order(orderPurchaseList, date));
        }
        return orderList;
    }

    public void addPurchaseList(Long userID, List<Purchase> purchaseList) {
        String insertQuery = sqlQueryGenerator.generateQueryForClass(SqlQueryType.INSERT, Purchase.class);
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            SqlPooledConnection connection = daoSession.getConnection();
            for (Purchase purchase : purchaseList) {
                try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                    //inserting dependency entities
                    Dao<Price> priceDao = daoSession.getDao(Price.class);
                    Price insertedPrice = priceDao.insert(purchase.getPrice());
                    //try to find exist date and status with the same value and use them instead inserting
                    Dao<Date> dateDao = daoSession.getDao(Date.class);
                    Dao<Status> statusDao = daoSession.getDao(Status.class);
                    Date insertedDate = dateDao.findFirstByParameter("time", purchase.getDate().getTime());
                    Status insertedStatus = statusDao.findFirstByParameter("name", purchase.getStatus().getName());
                    if (insertedDate == null) insertedDate = dateDao.insert(purchase.getDate());
                    if (insertedStatus == null) insertedStatus = statusDao.insert(purchase.getStatus());
                    statement.setLong(1, userID);
                    statement.setLong(2, purchase.getProduct().getId());
                    statement.setLong(3, insertedPrice.getId());
                    statement.setLong(4, insertedDate.getId());
                    statement.setLong(5, insertedStatus.getId());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new ServiceException(e);
                }
            }
        }
    }
}
