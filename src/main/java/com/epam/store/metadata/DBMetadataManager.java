package com.epam.store.metadata;

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
    private Map<String, DatabaseTable> tables;
    private NameFormatter nameFormatter;

    public DBMetadataManager(DatabaseMetaData databaseMetaData) {
        nameFormatter = NameFormatter.getInstance();
        tables = createTables(databaseMetaData);
    }

    public DatabaseTable getTableForClass(Class<? extends BaseEntity> entity) {
        String tableName = nameFormatter.getTableNameForClass(entity);
        return tables.get(tableName);
    }

    private Map<String, DatabaseTable> createTables(DatabaseMetaData databaseMetaData) {
        Map<String, DatabaseTable> tables = new HashMap<>();
        Map<String, List<String>> columnNamesByTableName = getColumnsByTableNameMap(databaseMetaData);
        for (Map.Entry<String, List<String>> entry : columnNamesByTableName.entrySet()) {
            String tableName = entry.getKey();
            List<String> columnNames = entry.getValue();
            List<DatabaseColumn> columns = createColumns(tableName, columnNames, databaseMetaData);
            String tablePrimaryKeyName = nameFormatter.getPrimaryKeyNameForTable(tableName);
            DatabaseTable databaseTable = new DatabaseTable(tableName, tablePrimaryKeyName, columns);
            tables.put(tableName, databaseTable);
        }
        return tables;
    }

    private List<DatabaseColumn> createColumns(String tableName, List<String> columnNames, DatabaseMetaData databaseMetaData) {
        List<DatabaseColumn> columnsList = new ArrayList<>();
        List<String> uniqueConstraintColumns = getUniqueConstraintColumns(tableName, databaseMetaData);
        for (String columnName : columnNames) {
            String fieldName = nameFormatter.getFieldNameFromColumnName(columnName);
            boolean foreignKey = isColumnForeignID(tableName, columnName);
            boolean unique = uniqueConstraintColumns.contains(columnName);
            DatabaseColumn column = new DatabaseColumn(columnName, fieldName, foreignKey, unique);
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
            throw new MetadataException(e);
        }
        return columnsByTableName;
    }

    private List<String> getUniqueConstraintColumns(String tableName, DatabaseMetaData databaseMetaData) {
        ArrayList<String> uniqueColumns = new ArrayList<>();
        try {
            ResultSet indexInfo = databaseMetaData.getIndexInfo(null, null, tableName, true, false);
            while (indexInfo.next()) {
                String columnName = indexInfo.getString("COLUMN_NAME");
                boolean unique = !indexInfo.getBoolean("NON_UNIQUE");
                if (unique) uniqueColumns.add(columnName);
            }
        } catch (SQLException e) {
            throw new MetadataException(e);
        }
        return uniqueColumns;
    }

    private boolean isColumnForeignID(String tableName, String columnName) {
        return columnName.endsWith(DatabaseColumn.ID_SUFFIX) && !columnName.startsWith(tableName);
    }

    private boolean isColumnUnnecessary(String tableName, String columnName) {
        return columnName.equals(nameFormatter.getPrimaryKeyNameForTable(tableName))
                || columnName.equalsIgnoreCase(DELETED_COLUMN_NAME);
    }
}
