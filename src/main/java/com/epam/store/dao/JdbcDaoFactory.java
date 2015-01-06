package com.epam.store.dao;

import com.epam.store.SqlQueryManager;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.BaseEntity;

import java.sql.SQLException;

public class JdbcDaoFactory implements DaoFactory {
    private ConnectionPool connectionPool;
    private DBMetadataManager dbMetadataManager;
    private SqlQueryManager sqlQueryManager;

    public JdbcDaoFactory(ConnectionPool connectionPool, SqlQueryManager sqlQueryManager) {
        this.connectionPool = connectionPool;
        this.sqlQueryManager = sqlQueryManager;
        try (SqlPooledConnection connection = connectionPool.getConnection()) {
            dbMetadataManager = new DBMetadataManager(connection.getMetaData());
        } catch (SQLException e) {
            throw new DaoException(e);
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
            return new JdbcDao<>
                    (this, clazz, sqlQueryManager, dbMetadataManager.getTableForClass(clazz));
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
