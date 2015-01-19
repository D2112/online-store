package com.epam.store.service;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Purchase;
import com.epam.store.model.User;

import java.util.List;

public class UserService {
    DaoFactory daoFactory;

    public UserService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public User findUser(String email) {
        User user;
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            List<User> userList = userDao.findByParameter("email", email);
            if (userList.size() > 1) throw new ServiceException("More than one user was found");
            if (userList.size() == 0) return null;
            user = userList.get(0);
        }
        return user;
    }

    public List<User> getAllUsers() {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            return userDao.getAll();
        }
    }

    public List<Purchase> getUserPurchaseList(User user) {
        List<Purchase> purchaseList;
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Purchase> purchaseDao = daoSession.getDao(Purchase.class);
            purchaseList = purchaseDao.findByParameter("USER_ID", user.getId());
        }
        return purchaseList;
    }

}
