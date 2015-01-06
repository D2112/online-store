package com.epam.store.dbpool;

public abstract class ConnectionPool {

    public static ConnectionPool getInstance() {
        return InstanceHolder.instance;
    }

    public abstract SqlPooledConnection getConnection();

    protected ConnectionPool() {
    }

    private static class InstanceHolder {
        private static final ConnectionPool instance = new SqlConnectionPool();
    }
}
