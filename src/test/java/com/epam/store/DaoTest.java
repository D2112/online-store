package com.epam.store;

import org.junit.Assert;


public class DaoTest extends Assert {

    @org.junit.Test
    public void InsertAndReadSameUserFindByParametersThenDeleteAndTryFind() {
/*        DaoFactory daoFactory = new JdbcDaoFactory();
        DaoSession daoSession = daoFactory.getDaoSession();
        Dao<User> userDao = daoSession.getDao(User.class);
        User user = new User("Test", "Test@gmail.com", new Password("hashHere", "SaltHere"));
        User insertedUser = userDao.insert(user);
        user.setId(insertedUser.getId());
        assertEquals(user, insertedUser);
        User readUser = userDao.find(insertedUser.getId());
        assertEquals(user, readUser);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", user.getName());
        parameters.put("Email", user.getEmail());
        List<User> list = userDao.findByParameters(parameters);
        User foundUser = list.get(0);
        assertTrue(list.size() >= 1);
        assertEquals(user, foundUser);
        boolean deleted = userDao.delete(user.getId());
        assertTrue(deleted);
        userDao.findByParameters(parameters);*/
    }
}
