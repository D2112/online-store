package com.epam.store.dbpool;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public interface SqlPooledConnection extends AutoCloseable {
    public PreparedStatement prepareStatement(String sql);

    public Statement createStatement();

    public DatabaseMetaData getMetaData();

    public void close();

    public void setAutoCommit(boolean b) throws SQLException;

    public void commit() throws SQLException;

    public void rollBack() throws SQLException;

    public boolean isValid();
}
