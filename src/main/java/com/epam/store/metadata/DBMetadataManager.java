package com.epam.store.metadata;

import com.epam.store.NameFormatter;
import com.epam.store.model.BaseEntity;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBMetadataManager {
    private static final String DELETED_COLUMN_NAME = "DELETED";
    private Map<String, List<String>> columnNamesByTableName;
    private Map<Class, DatabaseTable> tables;
    private NameFormatter nameFormatter;

    public DBMetadataManager(DatabaseMetaData databaseMetaData) {
        nameFormatter = NameFormatter.getInstance();
        tables = new HashMap<>();
        columnNamesByTableName = getColumnsByTableNameMap(databaseMetaData);
    }

    public DatabaseTable getTableForClass(Class<? extends BaseEntity> entity) {
        if (tables.containsKey(entity)) return tables.get(entity);
        DatabaseTable table = createTable(nameFormatter.getTableNameForClass(entity));
        tables.put(entity, table);
        return table;
    }

    private DatabaseTable createTable(String tableName) {
        List<String> columnsNames = columnNamesByTableName.get(tableName);
        List<DatabaseColumn> columns = createColumns(tableName, columnsNames);
        String tablePrimaryKeyName = nameFormatter.getPrimaryKeyNameForTable(tableName);
        return new DatabaseTable(tableName, tablePrimaryKeyName, columns);
    }

    private List<DatabaseColumn> createColumns(String tableName, List<String> columnNames) {
        List<DatabaseColumn> columnsList = new ArrayList<>();
        for (String columnName : columnNames) {
            String fieldName = nameFormatter.getFieldNameFromColumnName(columnName);
            boolean foreignKey = isColumnForeignID(tableName, columnName);
            DatabaseColumn column = new DatabaseColumn(columnName, fieldName, foreignKey);
            columnsList.add(column);
        }
        return columnsList;
    }

    private Map<String, List<String>> getColumnsByTableNameMap(DatabaseMetaData databaseMetaData) {
        Map<String, List<String>> columnsByTableName = new HashMap<>();
        try {
            ResultSet tables = databaseMetaData.getTables(null, "PUBLIC", null, null);
            while (tables.next()) {
                String tableName = tables.getString(3);
                ResultSet columnsResultSet = databaseMetaData.getColumns(null, null, tableName, null);
                List<String> columnsList = new ArrayList<>();
                while (columnsResultSet.next()) {
                    String columnName = columnsResultSet.getString("COLUMN_NAME");
                    if (isColumnUnnecessary(tableName, columnName)) continue;
                    columnsList.add(columnName);
                }
                columnsByTableName.put(tableName, columnsList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnsByTableName;
    }

    private boolean isColumnForeignID(String tableName, String columnName) {
        return columnName.endsWith(DatabaseColumn.ID_SUFFIX) && !columnName.startsWith(tableName);
    }

    private boolean isColumnUnnecessary(String tableName, String columnName) {
        return columnName.equals(nameFormatter.getPrimaryKeyNameForTable(tableName))
                || columnName.equalsIgnoreCase(DELETED_COLUMN_NAME);
    }
}
