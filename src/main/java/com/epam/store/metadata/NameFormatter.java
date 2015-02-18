package com.epam.store.metadata;

public class NameFormatter {
    private static final NameFormatter instance = new NameFormatter();
    private static final char WORD_SEPARATOR = '_';

    public static String getTableNameForClass(Class clazz) {
        String className = clazz.getSimpleName();
        if (isCamelCase(className)) {
            return getTableNameForCamelCase(className);
        }
        return className.toUpperCase();
    }

    public static String getFieldNameFromColumnName(String columnName) {
        StringBuilder sb = new StringBuilder();
        if (columnName.endsWith(DatabaseColumn.ID_SUFFIX)) {
            columnName = removeIdSuffix(columnName);
        }
        columnName = columnName.toLowerCase();
        char[] chars = columnName.toCharArray();
        int index = 0;
        //remove low-dash separators and substitute uppercase char instead to get standard java camelcase field name
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

    public static String getPrimaryKeyNameForTable(String tableName) {
        return tableName + DatabaseColumn.ID_SUFFIX;
    }

    public static boolean isCamelCase(String str) {
        for (char ch : str.substring(1).toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    private static String getTableNameForCamelCase(String str) {
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

    private static String removeIdSuffix(String str) {
        return str.substring(0, str.length() - DatabaseColumn.ID_SUFFIX.length());
    }
}
