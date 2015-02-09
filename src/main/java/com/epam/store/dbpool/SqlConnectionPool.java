package com.epam.store.dbpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
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

    /**
     * Closes all connections regardless of is connection used right now or not
     *
     * @throws PoolException if can't close one of the connections
     */
    public void shutdown() {
        int closedCount = availableConnections.size() + usedConnections.size();
        try {
            closeConnections(availableConnections);
            closeConnections(usedConnections);
        } catch (SQLException e) {
            String errorMessage = "Can't close connection pool";
            log.error(errorMessage, e);
            throw new PoolException(errorMessage, e);
        }
        log.info("The connection pool closed successfully. " +
                "{} connections have been closed", closedCount);
    }

    /**
     * Gets available connection from the available connections list
     * Before getting the connection method checks whether the list size is 0
     * if it is, then probably database was down and connection pool
     * needs to initialize with connection again. Otherwise method iterates
     * the list and get first alive connection
     * at the same time removes dead connections if such are present.
     * If there is no alive connections it means database is or was down.
     * At last method shall try to return new connection, if database down at present
     * will be thrown {@link PoolException}
     *
     * @return PooledConnection
     * @throws PoolException if all available connections is dead
     */
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
                availableConnections.iterator().remove();//remove dead connection
            }
        }
        return createConnection();
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

    private boolean closeConnections(List<PooledConnection> connections) throws SQLException {
        int closed = 0;
        for (PooledConnection connection : connections) {
            if (availableConnections.remove(connection) || usedConnections.remove(connection)) {
                connection.getConnection().close();
            }
        }
        return closed == connections.size();
    }

    private PooledConnection createConnection() {
        PooledConnection pooledConnection;
        try {
            Connection connection = DriverManager.getConnection(config.url(), config.username(), config.password());
            pooledConnection = new PooledConnection(connection);
        } catch (SQLException e) {
            String errorMessage = "Error while creating connection: " + e.getMessage() + ", probably database is down";
            log.error(errorMessage, e);
            throw new PoolException(errorMessage, e);
        }
        return pooledConnection;
    }

    private void initializePoolWithMinimumConnections() {
        for (int i = 0; i < config.minConnections(); i++) {
            availableConnections.add(createConnection());
        }
    }

    /**
     * A wrapper for {@link Connection} class, instead closing the connection
     * it delegates to connection pool for closing. And also it have
     * last access time for knowing if connection expired
     */
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

    /**
     * Collects all timeout and redundant connections
     * regularly after a certain time
     * (the time depends on config connectionIdleTimeout)
     * and delegates them to connection pool method
     * {@link #closeConnections(List)} for closing
     */
    private class ConnectionCollector extends TimerTask {

        @Override
        public void run() {
            if (availableConnections.size() == config.minIdleConnections()) return;
            lock.lock();
            try {
                List<PooledConnection> connectionsToClose = new ArrayList<>();
                connectionsToClose.addAll(collectTimeoutConnections());
                connectionsToClose.addAll(collectRedundantConnections());
                closeConnections(connectionsToClose);
            } catch (SQLException e) {
                String errorMessage = "Error on close connection: " + e.getMessage();
                log.error(errorMessage, e);
                throw new PoolException(errorMessage, e);
            } finally {
                lock.unlock();
            }
        }

        private List<PooledConnection> collectTimeoutConnections() {
            List<PooledConnection> connectionsToClose = new ArrayList<>();
            for (PooledConnection pooledConnection : availableConnections) {
                if (availableConnections.size() > config.minIdleConnections()) {
                    long currentTime = System.currentTimeMillis();
                    long lastAccessTime = pooledConnection.getLastAccessTimeStamp();
                    long idleTime = currentTime - ((lastAccessTime == 0) ? currentTime : lastAccessTime);
                    if (idleTime > config.connectionIdleTimeout()) {
                        connectionsToClose.add(pooledConnection);
                    }
                }
            }
            return connectionsToClose;
        }

        private List<PooledConnection> collectRedundantConnections() {
            List<PooledConnection> connectionsToClose = new ArrayList<>();
            int redundantAmount = availableConnections.size() - config.maxIdleConnections();
            if (redundantAmount > 0) {
                for (int i = 0; i < redundantAmount; i++) {
                    connectionsToClose.add(availableConnections.get(i));
                }
            }
            return connectionsToClose;
        }
    }
}
