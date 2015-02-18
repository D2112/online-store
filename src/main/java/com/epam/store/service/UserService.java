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
    private static final String USER_EMAIL_COLUMN = "EMAIL";
    private static final String ROLE_NAME_COLUMN = "NAME";
    private static final String ROLE_ID_COLUMN = "ROLE_ID";
    private static final String USER_ID_COLUMN = "USER_ID";
    private static final String DATE_TIME_COLUMN = "TIME";
    private static final String STATUS_NAME_COLUMN = "NAME";
    private DaoFactory daoFactory;
    private SqlQueryFactory sqlQueryFactory;

    public UserService(DaoFactory daoFactory, SqlQueryFactory sqlQueryFactory) {
        this.daoFactory = daoFactory;
        this.sqlQueryFactory = sqlQueryFactory;
    }

    public User findUser(String email) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            return userDao.findFirstByParameter(USER_EMAIL_COLUMN, email);
        }
    }

    public boolean isUserExist(String email) {
        return findUser(email) != null;
    }

    public User registerUser(String name, String email, String password) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            if (findUser(email) != null) throw new ServiceException("User with such email already registered");
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            Dao<Role> roleDao = daoSession.getDao(Role.class);
            Role userRole = roleDao.findFirstByParameter(ROLE_NAME_COLUMN, Role.USER_ROLE_NAME);
            user.setRole(userRole);
            user.setPassword(PasswordEncryptor.encrypt(password));
            return userDao.insert(user);
        }
    }

    /**
     * Checks if in database exist user with such email and password.
     * And return found user if exist.
     *
     * @param email    user's email
     * @param password user's password
     * @return User object from database, or null, if such user didn't found
     */
    public User authenticateUser(String email, String password) {
        User foundUser = findUser(email);
        if (foundUser != null && PasswordEncryptor.comparePassword(password, foundUser.getPassword())) {
            return foundUser;
        }
        return null;
    }


    public List<User> getAllUsers() {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            return userDao.findByParameter(ROLE_ID_COLUMN, Role.USER_ROLE_ID);
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

    /**
     * Gets user's purchase list and forms with it order list.
     * Order is list of purchases committed at one time
     */
    public List<Order> getUserOrderList(long userID) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Purchase> purchaseDao = daoSession.getDao(Purchase.class);
            List<Purchase> purchaseList = purchaseDao.findByParameter(USER_ID_COLUMN, userID);
            return getOrderList(purchaseList);
        }
    }

    public void addPurchaseListToUser(Long userID, List<Purchase> purchaseList) {
        SqlQuery insertQuery = sqlQueryFactory.getQueryForClass(SqlQueryType.INSERT, Purchase.class);
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            SqlPooledConnection connection = daoSession.getConnection();
            for (Purchase purchase : purchaseList) {
                try (PreparedStatement statement = connection.prepareStatement(insertQuery.getQuery())) {
                    //inserting dependency entities
                    Dao<Price> priceDao = daoSession.getDao(Price.class);
                    Price insertedPrice = priceDao.insert(purchase.getPrice());
                    //try to find exist date and status with the same value and use them instead inserting
                    Dao<Date> dateDao = daoSession.getDao(Date.class);
                    Dao<Status> statusDao = daoSession.getDao(Status.class);
                    Date insertedDate = dateDao.findFirstByParameter(DATE_TIME_COLUMN, purchase.getDate().getTime());
                    Status insertedStatus = statusDao.findFirstByParameter(STATUS_NAME_COLUMN, purchase.getStatus().getName());
                    if (insertedDate == null) insertedDate = dateDao.insert(purchase.getDate());
                    if (insertedStatus == null) insertedStatus = statusDao.insert(purchase.getStatus());
                    statement.setLong(1, userID); //user id
                    statement.setLong(2, purchase.getProduct().getId()); // purchase id
                    statement.setLong(3, insertedPrice.getId()); // price id
                    statement.setLong(4, insertedDate.getId()); // date id
                    statement.setLong(5, insertedStatus.getId()); //status id
                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new ServiceException(e);
                }
            }
        }
    }

    private Set<Date> getUniqueDatesFromPurchaseList(List<Purchase> purchaseList) {
        Set<Date> dates = new HashSet<>();
        for (Purchase purchase : purchaseList) {
            dates.add(purchase.getDate());
        }
        return dates;
    }

    private List<Order> getOrderList(List<Purchase> purchaseList) {
        List<Order> orderList = new ArrayList<>();
        Set<Date> dates = getUniqueDatesFromPurchaseList(purchaseList);
        for (Date date : dates) {
            List<Purchase> orderPurchaseList = purchaseList.stream()
                    .filter(purchase -> purchase.getDate().equals(date)) //get all purchases with the same date
                    .collect(Collectors.toList());
            orderList.add(new Order(orderPurchaseList, date)); //form with them order
        }
        return orderList;
    }
}
