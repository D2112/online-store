package com.epam.store.dbpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SqlConnectionPool extends ConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(SqlConnectionPool.class);
    private Queue<PooledConnection> availableConnections;
    private ConnectionPoolConfig config;
    private int usedConnectionsAmount;
    private Lock lock;
    private Condition hasAvailableConnection;

    public SqlConnectionPool() {
        config = new ConnectionPoolConfig();
        availableConnections = new LinkedList<>();
        lock = new ReentrantLock();
        hasAvailableConnection = lock.newCondition();
        try {
            Class.forName(config.driver());
        } catch (ClassNotFoundException e) {
            log.error("Can't find driver class ", e);
            throw new PoolException("Can't find driver class " + config.driver());
        }
        Timer timer = new Timer("Collector Timer", true);
        timer.schedule(new ConnectionCollector(), 0, config.connectionIdleTimeout());

        //initializing pool with connections
        for (int i = 0; i < config.minConnections(); i++) {
            availableConnections.add(createConnection());
        }
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
            PooledConnection connection = getAvailableConnection();
            usedConnectionsAmount++;
            return connection;
        } finally {
            lock.unlock();
        }
    }

    private PooledConnection getAvailableConnection() {
        if (availableConnections.size() == 0) {
            return createConnection();
        }
        return availableConnections.poll();
    }

    private boolean noAvailableConnections() {
        return usedConnectionsAmount == config.maxConnections() && availableConnections.size() == 0;
    }

    private void releaseConnection(PooledConnection connection) {
        lock.lock();
        try {
            if (availableConnections.contains(connection)) return;
            connection.setLastAccessTimeStamp(System.currentTimeMillis());
            availableConnections.add(connection);
            usedConnectionsAmount--;
            hasAvailableConnection.signal();
        } finally {
            lock.unlock();
        }
    }

    private PooledConnection createConnection() {
        PooledConnection connection;
        try {
            connection = new PooledConnection(
                    DriverManager.getConnection(config.url(), config.username(), config.password()));
        } catch (SQLException e) {
            log.error("error while creating connection: " + e.getMessage(), e);
            throw new PoolException("error while creating connection: " + e.getMessage());
        }
        return connection;
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
                throw new PoolException(e);
            }
        }

        @Override
        public DatabaseMetaData getMetaData() {
            try {
                return connection.getMetaData();
            } catch (SQLException e) {
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
                throw new PoolException(e);
            }
        }

        @Override
        public void commit() {
            try {
                connection.commit();
            } catch (SQLException e) {
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
    }

    private class ConnectionCollector extends TimerTask {

        @Override
        public void run() {
            if (availableConnections.size() == config.minIdleConnections()) return;
            closeTimeoutConnections();
            closeRedundantConnections();
        }

        private void close(PooledConnection pooledConnection) {
            try {
                pooledConnection.getConnection().close();
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
                    close(availableConnections.poll());
                }
            }
        }
    }
}
