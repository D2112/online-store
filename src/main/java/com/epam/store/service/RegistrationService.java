package com.epam.store.service;

import com.epam.store.PasswordEncryptor;
import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Role;
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
            //check if user with the same email exist
            User userByEmail = userDao.findFirstByParameter("email", email);
            if (userByEmail != null) return false;
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            Dao<Role> roleDao = daoSession.getDao(Role.class);
            List<Role> roleList = roleDao.findByParameter("name", Role.USER_ROLE_NAME);
            if (roleList.size() != 1) throw new ServiceException("Found more than one role for user");
            Role userRole = roleList.iterator().next();
            user.setRole(userRole);
            user.setPassword(passwordEncryptor.encrypt(password));
            userDao.insert(user);
        }
        return true;
    }
}
