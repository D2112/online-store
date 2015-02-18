package com.epam.store.metadata;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTable {
    private String tableName;
    private DatabaseColumn primaryKeyColumn;
    private List<DatabaseColumn> columns;

    DatabaseTable(String tableName, DatabaseColumn primaryKeyColumnName, List<DatabaseColumn> columns) {
        this.tableName = tableName;
        this.primaryKeyColumn = primaryKeyColumnName;
        this.columns = columns;
    }

    public List<DatabaseColumn> getColumns() {
        return new ArrayList<>(columns);
    }

    public String getName() {
        return tableName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyColumn.getName();
    }

    public DatabaseColumn getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseTable that = (DatabaseTable) o;

        if (columns != null ? !columns.equals(that.columns) : that.columns != null) return false;
        if (primaryKeyColumn != null ? !primaryKeyColumn.equals(that.primaryKeyColumn) : that.primaryKeyColumn != null)
            return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (primaryKeyColumn != null ? primaryKeyColumn.hashCode() : 0);
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DatabaseTable{" +
                "tableName='" + tableName + '\'' +
                ", primaryKeyColumn=" + primaryKeyColumn +
                ", columns=" + columns +
                '}';
    }
}
