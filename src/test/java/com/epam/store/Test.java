package com.epam.store;

import com.epam.store.dao.*;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.Date;
import com.epam.store.model.Image;

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
        DaoSession daoSession = daoFactory.getDaoSession();
        Dao<Image> dao = daoSession.getDao(Image.class);
        Image image = dao.find(40);
        Date date = new Date(image.getLastModified().getTime());
        dao.insert(image);
        System.out.println(new Date());
        System.out.println(date);
        daoSession.close();
        cp.shutdown();
    }
}
