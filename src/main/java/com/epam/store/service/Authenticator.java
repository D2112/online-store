package com.epam.store.service;

import com.epam.store.PasswordEncryptor;
import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.User;

import java.util.List;

public class Authenticator {
    private PasswordEncryptor passwordEncryptor;
    private DaoFactory daoFactory;

    public Authenticator(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        passwordEncryptor = PasswordEncryptor.getInstance();
    }

    public User authenticate(String login, String password) {
        User authenticatedUser = null;
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            List<User> list = userDao.findByParameter("email", login);
            if (list.size() != 1) return null;
            User foundUser = list.get(0);
            if (foundUser != null && passwordEncryptor.comparePassword(password, foundUser.getPassword())) {
                authenticatedUser = foundUser;
            }
        }
        return authenticatedUser;
    }
}
