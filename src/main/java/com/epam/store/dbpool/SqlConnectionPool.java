package com.epam.store.dbpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SqlConnectionPool implements ConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(SqlConnectionPool.class);
    private ConnectionPoolConfig config;
    private List<PooledConnection> availableConnections;
    private List<PooledConnection> usedConnections;
    private Lock lock;
    private Condition hasAvailableConnection;

    public SqlConnectionPool() {
        config = new ConnectionPoolConfig();
        availableConnections = new LinkedList<>();
        usedConnections = new LinkedList<>();
        lock = new ReentrantLock();
        hasAvailableConnection = lock.newCondition();
        try {
            Class.forName(config.driver());
        } catch (ClassNotFoundException e) {
            String errorMessage = "Can't find driver class " + config.driver();
            log.error(errorMessage, e);
            throw new PoolException(errorMessage);
        }
        Timer timer = new Timer("Connection collector Timer", true);
        timer.schedule(new ConnectionCollector(), 0, config.connectionIdleTimeout());

        initializePoolWithMinimumConnections();
        log.info("Connection pool is initialized successfully. Available connections: " + availableConnections.size());
    }

    public SqlPooledConnection getConnection() {
        lock.lock();
        try {
            while (noAvailableConnections()) {
                try {
                    hasAvailableConnection.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("InterruptedException in thread: " + Thread.currentThread().getName(), e);
                }
            }
            PooledConnection availableConnection = getAvailableConnection();
            availableConnections.remove(availableConnection);
            usedConnections.add(availableConnection);
            return availableConnection;
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        int closedCount = 0;
        try {
            for (PooledConnection pooledConnection : availableConnections) {
                closeConnection(pooledConnection);
                closedCount++;
            }
            for (PooledConnection usedConnection : usedConnections) {
                closeConnection(usedConnection);
                closedCount++;
            }
        } catch (SQLException e) {
            String errorMessage = "Can't close connection pool";
            log.error(errorMessage, e);
            throw new PoolException(errorMessage, e);
        }
        log.info("The connection pool closed successfully. " +
                "{} connections have been closed", closedCount);
    }

    private PooledConnection getAvailableConnection() {
        if (availableConnections.size() == 0) {
            initializePoolWithMinimumConnections();
            return availableConnections.iterator().next();
        }
        for (PooledConnection availableConnection : availableConnections) {
            //check for the case if database is down
            if (availableConnection.isAlive()) {
                return availableConnection;
            } else {
                availableConnections.remove(availableConnection);//remove dead connection
            }
        }
        throw new PoolException("Database is down");
    }

    private boolean noAvailableConnections() {
        return (availableConnections.size() == 0) && (config.maxConnections() == usedConnections.size());
    }

    private void releaseConnection(PooledConnection connection) {
        lock.lock();
        try {
            if (usedConnections.remove(connection)) {
                connection.setLastAccessTimeStamp(System.currentTimeMillis());
                availableConnections.add(connection);
                hasAvailableConnection.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean closeConnection(PooledConnection connection) throws SQLException {
        connection.getConnection().close();
        return availableConnections.remove(connection) || usedConnections.remove(connection);
    }

    private PooledConnection createConnection() {
        PooledConnection pooledConnection;
        try {
            Connection connection = DriverManager.getConnection(config.url(), config.username(), config.password());
            pooledConnection = new PooledConnection(connection);
        } catch (SQLException e) {
            log.error("Error while creating connection: " + e.getMessage(), e);
            throw new PoolException("Error while creating connection: " + e.getMessage());
        }
        return pooledConnection;
    }

    private void initializePoolWithMinimumConnections() {
        for (int i = 0; i < config.minConnections(); i++) {
            availableConnections.add(createConnection());
        }
    }

    private boolean isDatabaseAlive() {
        return false;
    }

    private class PooledConnection implements SqlPooledConnection {
        private Connection connection;
        private long lastAccessTimeStamp;

        private PooledConnection(Connection connection) {
            this.connection = connection;
        }

        @Override
        public PreparedStatement prepareStatement(String sql) {
            try {
                return connection.prepareStatement(sql);
            } catch (SQLException e) {
                log.error("Error while getting preparedStatement from connection");
                throw new PoolException(e);
            }
        }

        @Override
        public Statement createStatement() {
            try {
                return connection.createStatement();
            } catch (SQLException e) {
                log.error("Error while creating statement");
                throw new PoolException(e);
            }
        }

        @Override
        public DatabaseMetaData getMetaData() {
            try {
                return connection.getMetaData();
            } catch (SQLException e) {
                log.error("Error while getting metadata from connection");
                throw new PoolException(e);
            }
        }

        @Override
        public void close() {
            releaseConnection(this);
        }

        @Override
        public void setAutoCommit(boolean b) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                log.error("Error while setting connection auto-commit");
                throw new PoolException(e);
            }
        }

        @Override
        public void commit() {
            try {
                connection.commit();
            } catch (SQLException e) {
                log.error("Error while committing");
                throw new PoolException(e);
            }
        }

        private Connection getConnection() {
            return connection;
        }

        private void setLastAccessTimeStamp(long time) {
            this.lastAccessTimeStamp = time;
        }

        private long getLastAccessTimeStamp() {
            return lastAccessTimeStamp;
        }

        private boolean isAlive() {
            try {
                return connection.isValid(config.getConnectionValidTimeout());
            } catch (SQLException e) {
                log.error("connection alive check has failed", e);
                return false;
            }
        }
    }

    private class ConnectionCollector extends TimerTask {

        @Override
        public void run() {
            if (availableConnections.size() == config.minIdleConnections()) return;
            lock.lock();
            try {
                closeTimeoutConnections();
                closeRedundantConnections();
            } finally {
                lock.unlock();
            }
        }

        private void close(PooledConnection pooledConnection) {
            try {
                closeConnection(pooledConnection);
            } catch (SQLException e) {
                log.error("Error on close connection: " + e.getMessage(), e);
                throw new PoolException("Error on close connection: " + e.getMessage());
            }
        }

        private void closeTimeoutConnections() {
            for (PooledConnection pooledConnection : availableConnections) {
                if (availableConnections.size() > config.minIdleConnections()) {
                    long currentTime = System.currentTimeMillis();
                    long lastAccessTime = pooledConnection.getLastAccessTimeStamp();
                    long idleTime = currentTime - ((lastAccessTime == 0) ? currentTime : lastAccessTime);
                    if (idleTime > config.connectionIdleTimeout()) {
                        close(pooledConnection);
                    }
                }
            }
        }

        private void closeRedundantConnections() {
            int redundantAmount = availableConnections.size() - config.maxIdleConnections();
            if (redundantAmount > 0) {
                for (int i = 0; i < redundantAmount; i++) {
                    close(availableConnections.get(i));
                }
            }
        }
    }
}
