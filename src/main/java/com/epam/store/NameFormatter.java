package com.epam.store;

import com.epam.store.metadata.DatabaseColumn;

public class NameFormatter {
    private static final NameFormatter instance = new NameFormatter();
    private static final char WORD_SEPARATOR = '_';

    public static NameFormatter getInstance() {
        return instance;
    }

    private NameFormatter() {
    }

    public String getTableNameForClass(Class clazz) {
        String className = clazz.getSimpleName();
        if (isCamelCase(className)) {
            return getTableNameForCamelCase(className);
        }
        return className.toUpperCase();
    }

    public String getFieldNameFromColumnName(String columnName) {
        StringBuilder sb = new StringBuilder();
        if (columnName.endsWith(DatabaseColumn.ID_SUFFIX)) {
            columnName = columnName.substring(0, columnName.length() - DatabaseColumn.ID_SUFFIX.length());
        }
        columnName = columnName.toLowerCase();
        char[] chars = columnName.toCharArray();
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == WORD_SEPARATOR) {
                sb.append(columnName.substring(index, i));
                sb.append(Character.toUpperCase(columnName.charAt(++i)));
                index = ++i;
            }
        }
        sb.append(columnName.substring(index));
        return sb.toString();
    }

    public String getPrimaryKeyNameForTable(String tableName) {
        return tableName + DatabaseColumn.ID_SUFFIX;
    }

    public boolean isCamelCase(String str) {
        for (char ch : str.substring(1).toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    private String getTableNameForCamelCase(String str) {
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        int underscoreIndex = 0;
        for (int i = 1; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])) {
                sb.append(str.substring(underscoreIndex, i));
                underscoreIndex = i;
                sb.append(WORD_SEPARATOR);
            }
        }
        sb.append(str.substring(underscoreIndex, chars.length));
        return sb.toString().toUpperCase();
    }
}
