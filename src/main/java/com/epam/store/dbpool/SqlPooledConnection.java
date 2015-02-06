package com.epam.store.dbpool;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;

public interface SqlPooledConnection extends AutoCloseable {
    public PreparedStatement prepareStatement(String sql);

    public Statement createStatement();

    public DatabaseMetaData getMetaData();

    public void close();

    public void setAutoCommit(boolean b);

    public void commit();

}
