package com.epam.store.service;

import com.epam.store.PasswordEncryptor;
import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Password;
import com.epam.store.model.Role;
import com.epam.store.model.User;

import java.util.List;

public class UserService {
    private static final String USER_EMAIL_COLUMN = "EMAIL";
    private static final String ROLE_NAME_COLUMN = "NAME";
    private static final String ROLE_ID_COLUMN = "ROLE_ID";
    private DaoFactory daoFactory;

    public UserService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public User findUser(String email) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            return userDao.findFirstByParameter(USER_EMAIL_COLUMN, email);
        }
    }

    public User findUser(long userID) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            return userDao.find(userID);
        }
    }

    public void changeUserPassword(User user, String newPassword) {
        Password encryptedPassword = PasswordEncryptor.encrypt(newPassword.getBytes());
        user.setPassword(encryptedPassword);
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            userDao.update(user);
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
            user.setPassword(PasswordEncryptor.encrypt(password.getBytes()));
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
        if (foundUser != null && PasswordEncryptor.comparePassword(password.getBytes(), foundUser.getPassword())) {
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
}
