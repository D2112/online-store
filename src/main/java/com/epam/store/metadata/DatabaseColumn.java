package com.epam.store.metadata;

public class DatabaseColumn {
    public static final String ID_SUFFIX = "_ID";
    private String columnName;
    private String fieldName;
    private boolean primaryKey;
    private boolean foreignKey;
    private boolean unique;

    DatabaseColumn(boolean primaryKey, String columnName, boolean foreignKey, String fieldName, boolean unique) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.foreignKey = foreignKey;
        this.unique = unique;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseColumn column = (DatabaseColumn) o;

        if (foreignKey != column.foreignKey) return false;
        if (primaryKey != column.primaryKey) return false;
        if (unique != column.unique) return false;
        if (columnName != null ? !columnName.equals(column.columnName) : column.columnName != null) return false;
        if (fieldName != null ? !fieldName.equals(column.fieldName) : column.fieldName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = columnName != null ? columnName.hashCode() : 0;
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + (primaryKey ? 1 : 0);
        result = 31 * result + (foreignKey ? 1 : 0);
        result = 31 * result + (unique ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DatabaseColumn{" +
                "columnName='" + columnName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", primaryKey=" + primaryKey +
                ", foreignKey=" + foreignKey +
                ", unique=" + unique +
                '}';
    }
}
