package com.epam.store;

import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.JdbcDaoFactory;
import com.epam.store.dao.SqlQueryFactory;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.service.UserService;

import java.lang.reflect.InvocationTargetException;

public class Test {
    private static final String USER_EMAIL_COLUMN = "EMAIL";
    private static final String ROLE_NAME_COLUMN = "NAME";
    private static final String ROLE_ID_COLUMN = "ROLE_ID";
    private static final String USER_ID_COLUMN = "USER_ID";
    private static final String DATE_TIME_COLUMN = "TIME";
    private static final String STATUS_NAME_COLUMN = "NAME";

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        ConnectionPool cp = new SqlConnectionPool();
        SqlQueryFactory queryFactory;
        DBMetadataManager dbMetadataManager;
        try (SqlPooledConnection connection = cp.getConnection()) {
            dbMetadataManager = new DBMetadataManager(connection.getMetaData());
            queryFactory = new SqlQueryFactory(dbMetadataManager);
        }
        DaoFactory daoFactory = new JdbcDaoFactory(cp);
        UserService userService = new UserService(daoFactory);

        cp.shutdown();
    }
}
