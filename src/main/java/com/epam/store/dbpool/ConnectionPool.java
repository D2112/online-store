package com.epam.store.dbpool;

public interface ConnectionPool {
    public SqlPooledConnection getConnection();
    public void shutdown();
}
