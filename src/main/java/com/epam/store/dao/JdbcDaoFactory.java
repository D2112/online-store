package com.epam.store.dao;

import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.metadata.EntityManager;
import com.epam.store.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JdbcDaoFactory implements DaoFactory {
    private static final Logger log = LoggerFactory.getLogger(JdbcDao.class);
    private ConnectionPool connectionPool;
    private DBMetadataManager dbMetadataManager;
    private SqlQueryFactory sqlQueryFactory;
    private Map<Class, EntityManager> entityManagerCache = new HashMap<>();

    public JdbcDaoFactory(ConnectionPool connectionPool, SqlQueryFactory sqlQueryFactory) {
        this.connectionPool = connectionPool;
        this.sqlQueryFactory = sqlQueryFactory;
        try (SqlPooledConnection connection = connectionPool.getConnection()) {
            dbMetadataManager = new DBMetadataManager(connection.getMetaData());
        }
    }

    public DaoSession getDaoSession() {
        return new JdbcDaoSession(connectionPool.getConnection());
    }

    private class JdbcDaoSession implements DaoSession {
        private SqlPooledConnection connection;

        public JdbcDaoSession(SqlPooledConnection connection) {
            this.connection = connection;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends BaseEntity> Dao<T> getDao(Class<T> clazz) {
            EntityManager<T> entityManager = getEntityManagerForClass(clazz);
            return new JdbcDao<>(this, clazz, entityManager, sqlQueryFactory, dbMetadataManager.getTableForClass(clazz));
        }

        @Override
        public void beginTransaction() {
            connection.setAutoCommit(false);
        }

        @Override
        public void endTransaction() {
            connection.commit();
            connection.setAutoCommit(true);
        }

        @Override
        public void close() {
            connection.close();
        }

        @Override
        public SqlPooledConnection getConnection() {
            return connection;
        }
    }

    private synchronized void addToCache(Class clazz, EntityManager entityManager) {
        entityManagerCache.put(clazz, entityManager);
    }

    @SuppressWarnings("unchecked")
    private <T> EntityManager getEntityManagerForClass(Class<T> clazz) {
        EntityManager<T> entityManager;
        if (entityManagerCache.containsKey(clazz)) {
            log.debug("Getting entity manager from cache for class: " + clazz.getSimpleName());
            entityManager = entityManagerCache.get(clazz);
        } else {
            entityManager = new EntityManager<>(clazz);
            addToCache(clazz, entityManager);
        }
        return entityManager;
    }
}
