package com.epam.store.metadata;

import com.epam.store.model.BaseEntity;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains database tables and columns.
 * All data read only one time while creating instance of this class
 * So if need actual database information,
 * needs to create instance again.
 * The tables from this class doesn't contain primary key column
 * and the Deleted column
 */
public class DBMetadataManager {
    private Map<String, DatabaseTable> tables;

    public DBMetadataManager(DatabaseMetaData databaseMetaData) {
        tables = createTables(databaseMetaData);
    }

    /**
     * @param clazz needs to take name from it
     * @return A database table class {@link DatabaseTable}
     * @throws MetadataException if table with such name don't exist
     */
    public DatabaseTable getTableForClass(Class<? extends BaseEntity> clazz) {
        String tableName = NameFormatter.getTableNameForClass(clazz);
        return getTableByName(tableName);
    }

    public DatabaseTable getTableByName(String tableName) {
        DatabaseTable databaseTable = tables.get(tableName);
        if (databaseTable == null) throw new MetadataException("Can't find table " + tableName);
        return databaseTable;
    }

    private Map<String, DatabaseTable> createTables(DatabaseMetaData databaseMetaData) {
        Map<String, DatabaseTable> tables = new HashMap<>();
        Map<String, List<String>> columnNamesByTableName = getColumnsByTableNameMap(databaseMetaData);
        for (Map.Entry<String, List<String>> entry : columnNamesByTableName.entrySet()) {
            String tableName = entry.getKey();
            List<String> columnNames = entry.getValue();
            List<DatabaseColumn> columns = createColumns(tableName, columnNames, databaseMetaData);
            DatabaseColumn primaryKeyColumn = findPrimaryKeyColumn(tableName, columns);
            DatabaseTable databaseTable = new DatabaseTable(tableName, primaryKeyColumn, columns);
            tables.put(tableName, databaseTable);
        }
        return tables;
    }

    private DatabaseColumn findPrimaryKeyColumn(String tableName, List<DatabaseColumn> columns) {
        for (DatabaseColumn column : columns) {
            if (isColumnPrimaryKey(tableName, column.getName())) {
                return column;
            }
        }
        return null;
    }

    private List<DatabaseColumn> createColumns(String tableName, List<String> columnNames, DatabaseMetaData databaseMetaData) {
        List<DatabaseColumn> columnsList = new ArrayList<>();
        List<String> uniqueConstraintColumns = getUniqueConstraintColumns(tableName, databaseMetaData);
        for (String columnName : columnNames) {
            String fieldName = NameFormatter.getFieldNameFromColumnName(columnName);
            boolean foreignKey = isColumnForeignID(tableName, columnName);
            boolean unique = uniqueConstraintColumns.contains(columnName);
            boolean primaryKey = isColumnPrimaryKey(tableName, columnName);
            DatabaseColumn column = new DatabaseColumn(primaryKey, columnName, foreignKey, fieldName, unique);
            columnsList.add(column);
        }
        return columnsList;
    }

    private Map<String, List<String>> getColumnsByTableNameMap(DatabaseMetaData databaseMetaData) {
        Map<String, List<String>> columnsByTableName = new HashMap<>();
        try {
            ResultSet tablesResultSet = databaseMetaData.getTables(null, "PUBLIC", null, null);
            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString(3);
                ResultSet columnsResultSet = databaseMetaData.getColumns(null, null, tableName, null);
                List<String> columnsList = new ArrayList<>();
                while (columnsResultSet.next()) {
                    String columnName = columnsResultSet.getString("COLUMN_NAME");
                    columnsList.add(columnName);
                }
                columnsByTableName.put(tableName, columnsList);
                columnsResultSet.close();
            }
            tablesResultSet.close();
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
            indexInfo.close();
        } catch (SQLException e) {
            throw new MetadataException(e);
        }
        return uniqueColumns;
    }

    private boolean isColumnForeignID(String tableName, String columnName) {
        return columnName.endsWith(DatabaseColumn.ID_SUFFIX) && !columnName.startsWith(tableName);
    }

    private boolean isColumnPrimaryKey(String tableName, String columnName) {
        return columnName.startsWith(tableName) && columnName.endsWith(DatabaseColumn.ID_SUFFIX);
    }
}
