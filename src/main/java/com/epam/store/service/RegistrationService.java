package com.epam.store.service;

import com.epam.store.PasswordEncryptor;
import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.User;

import java.util.List;

public class RegistrationService {
    private DaoFactory daoFactory;
    private PasswordEncryptor passwordEncryptor;

    public RegistrationService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        passwordEncryptor = PasswordEncryptor.getInstance();
    }

    public boolean register(String name, String email, String password) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            List<User> userList = userDao.findByParameter("email", email);
            if (userList.size() > 0) return false;
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncryptor.encrypt(password));
            userDao.insert(user);
        }
        return true;
    }
}
