package com.epam.store.metadata;

import java.util.List;

public class DatabaseTable {
    private String tableName;
    private String primaryKeyColumnName;
    private List<DatabaseColumn> columns;

    DatabaseTable(String tableName, String primaryKeyColumnName, List<DatabaseColumn> columns) {
        this.tableName = tableName;
        this.primaryKeyColumnName = primaryKeyColumnName;
        this.columns = columns;
    }

    public List<DatabaseColumn> getColumns() {
        return columns;
    }

    public String getName() {
        return tableName;
    }

    public String getPrimaryKeyColumnName() {
        return primaryKeyColumnName;
    }
}
