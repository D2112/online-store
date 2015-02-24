package com.epam.store.dao;

import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

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
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                log.error("Error on start transaction");
            }
        }

        @Override
        public void endTransaction() {
            try {
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("End of the transaction has been failed", e);
                try {
                    connection.rollBack();
                } catch (SQLException exc) {
                    log.error("Error while rollback", e);
                }
            }
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
