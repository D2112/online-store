package com.epam.store.metadata;

public class DatabaseColumn {
    public static final String ID_SUFFIX = "_ID";
    private String columnName;
    private String fieldName;
    private boolean unique;
    private boolean foreignKey;

    DatabaseColumn(String columnName, String fieldName, boolean foreignKey, boolean unique) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.foreignKey = foreignKey;
        this.unique = unique;
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
}
