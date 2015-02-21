package com.epam.store;

import com.epam.store.dao.*;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.Password;
import com.epam.store.model.Role;
import com.epam.store.model.User;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DaoTest extends Assert {

    @org.junit.Test
    public void InsertAndReadSameUserFindByParametersThenDeleteAndTryFind() {
        ConnectionPool cp = new SqlConnectionPool();
        SqlQueryFactory queryFactory;
        DBMetadataManager dbMetadataManager;
        try (SqlPooledConnection connection = cp.getConnection()) {
            dbMetadataManager = new DBMetadataManager(connection.getMetaData());
            queryFactory = new SqlQueryFactory(dbMetadataManager);
        }
        DaoFactory daoFactory = new JdbcDaoFactory(cp);
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<User> userDao = daoSession.getDao(User.class);
            User user = new User(
                    "Test",
                    "Test@gmail.com",
                    new Role(Role.USER_ROLE_NAME),
                    new Password("hashHere", "SaltHere"),
                    false);
            User insertedUser = userDao.insert(user);
            user.setId(insertedUser.getId());
            assertEquals(user, insertedUser);
            User readUser = userDao.find(insertedUser.getId());
            assertEquals(user, readUser);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("name", user.getName());
            parameters.put("email", user.getEmail());
            List<User> list = userDao.findByParameters(parameters);
            User foundUser = list.get(0);
            assertTrue(list.size() >= 1);
            boolean deleted = userDao.delete(user.getId());
            assertTrue(deleted);
            userDao.findByParameters(parameters);
        }
        cp.shutdown();
    }
}
