package com.epam.store.dao;

import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcDaoFactory implements DaoFactory {
    private static final Logger log = LoggerFactory.getLogger(JdbcDao.class);
    private ConnectionPool cp;
    private DBMetadataManager dbMetadataManager;
    private SqlQueryFactory sqlQueryFactory;
    private DaoRegistry daoRegistry;

    public JdbcDaoFactory(ConnectionPool cp) {
        this.cp = cp;
        try (SqlPooledConnection connection = cp.getConnection()) {
            dbMetadataManager = new DBMetadataManager(connection.getMetaData());
            sqlQueryFactory = new SqlQueryFactory(dbMetadataManager);
        }
        daoRegistry = new DaoRegistry();
    }

    public DaoSession getDaoSession() {
        return new JdbcDaoSession(cp.getConnection());
    }

    private class JdbcDaoSession implements DaoSession {
        private SqlPooledConnection connection;

        public JdbcDaoSession(SqlPooledConnection connection) {
            this.connection = connection;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends BaseEntity> Dao<T> getDao(Class<T> clazz) {
            DaoCreator daoCreator = daoRegistry.get(clazz);
            return daoCreator.create(this, clazz, sqlQueryFactory, dbMetadataManager.getTableForClass(clazz));
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
}
