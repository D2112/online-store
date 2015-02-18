package com.epam.store.dao;

import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.metadata.DatabaseColumn;
import com.epam.store.metadata.DatabaseTable;
import com.epam.store.metadata.NameFormatter;
import com.epam.store.model.BaseEntity;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Format SQL query templates for different types of query
 * defined in {@link SqlQueryType}.
 * For work needs file {@link #QUERY_FILE_NAME} with query templates
 */
public class SqlQueryFactory {
    private static final Logger log = LoggerFactory.getLogger(SqlQueryFactory.class);
    private static final String QUERY_FILE_NAME = "query.properties";
    private static final String FIND_BY_PARAMETERS_QUERY_NAME = "FIND_BY_PARAMETERS";
    private static final String PUBLIC_TABLES_COUNT_QUERY_NAME = "PUBLIC_TABLES_COUNT";
    private static final String SEARCH_QUERY_PARAMETERS_SEPARATOR = " AND ";
    private static final String COMMA = ", ";
    private static final String WILDCARD = " ? ";
    private static final String DELETED_COLUMN_NAME = "DELETED";
    private static final ConcurrentTable<DatabaseTable, SqlQueryType, SqlQuery> queryCache = new ConcurrentTable<>();
    private Properties queries;
    private DBMetadataManager dbMetadataManager;


    public SqlQueryFactory(DatabaseMetaData metaData) {
        dbMetadataManager = new DBMetadataManager(metaData);
        queries = new Properties();
        try (InputStream inputStream = SqlQueryFactory.class.getClassLoader().getResourceAsStream(QUERY_FILE_NAME)) {
            queries.load(inputStream);
        } catch (IOException e) {
            throw new DaoException("Error reading sql query file", e);
        }
    }

    /**
     * Generates query of specified type,
     * the table name in query shall be taken from class.
     * Caches all generated queries.
     *
     * @param type        of query
     * @param entityClass - some entity class which collates with database table
     *                    needs to take table name from class name
     * @return Parametrized SQL query for Prepared Statement
     */
    public SqlQuery getQueryForClass(SqlQueryType type, Class<? extends BaseEntity> entityClass) {
        DatabaseTable databaseTable = dbMetadataManager.getTableForClass(entityClass);
        if (queryCache.contains(databaseTable, type)) {
            log.debug("Query from cache for table: " + databaseTable.getName() + ", with type " + type.name());
            return queryCache.get(databaseTable, type);
        }
        SqlQuery sqlQuery = generateQuery(type, databaseTable);
        queryCache.put(databaseTable, type, sqlQuery);
        return sqlQuery;
    }

    /**
     * Generates search query which returns database records
     * with values which are equals to specified parameters,
     * the table name in query shall be taken from class.
     *
     * @param entityClass     - some entity class which collates with database table
     *                        needs to take table name from class name
     * @param parametersNames which are equals to database column names
     * @return Parametrized SQL query for Prepared Statement
     */
    public String generateFindByParametersQuery(Class<? extends BaseEntity> entityClass,
                                                Collection<String> parametersNames) {
        String tableName = NameFormatter.getTableNameForClass(entityClass);
        String templateQuery = queries.getProperty(FIND_BY_PARAMETERS_QUERY_NAME);
        String templateWithTableName =
                String.format(templateQuery, tableName, "%s"); //substitute only table name
        return String.format(templateWithTableName, generateParametersWithNameString(parametersNames, true));
    }

    /**
     * Generates search query which returns database records
     * with values which are equals to specified parameter,
     * the table name in query shall be taken from class.
     *
     * @param entityClass   - some entity class which collates with database table
     *                      needs to take table name from class name
     * @param parameterName parameter which equal to database column name
     * @return Parametrized SQL query for Prepared Statement
     */
    public String generateFindByParameterQuery(Class<? extends BaseEntity> entityClass, String parameterName) {
        List<String> parameters = new ArrayList<>();
        parameters.add(parameterName);
        return generateFindByParametersQuery(entityClass, parameters);
    }

    /**
     * @return query for counting public tables in database
     */
    public String getPublicTablesCountQuery() {
        return queries.getProperty(PUBLIC_TABLES_COUNT_QUERY_NAME);
    }

    /**
     * Adds to query template table name.
     * Also adds string with parameters if it Update or Insert query,
     * depends on passed {@link SqlQueryType} parameter.
     *
     * @param table needs for data about columns for create query
     * @return completely finished sql query with wildcards(?) parameters
     * or without, depends on query type
     */
    private SqlQuery generateQuery(SqlQueryType type, DatabaseTable table) {
        List<DatabaseColumn> columns = table.getColumns();
        String templateQuery = queries.getProperty(type.name());
        String queryString;
        SqlQuery sqlQuery;
        if (type == SqlQueryType.INSERT) {
            sqlQuery = generateInsertQuery(templateQuery, table);
        } else if (type == SqlQueryType.UPDATE_BY_ID) {
            sqlQuery = generateUpdateQuery(templateQuery, table);
        } else if (type == SqlQueryType.READ_ALL || type == SqlQueryType.READ_LAST) {
            queryString = String.format(templateQuery, table.getName(), table.getPrimaryKeyName());
            sqlQuery = new SqlQueryImpl(queryString, table.getName()); //query without parameters
        } else {
            queryString = String.format(templateQuery, table.getName(), table.getPrimaryKeyName());
            sqlQuery = new SqlQueryImpl
                    (queryString, table.getName(), table.getPrimaryKeyColumn()); //query with only one parameter
        }
        log.debug("Generated query " + sqlQuery);
        return sqlQuery;
    }

    private SqlQuery generateInsertQuery(String templateQuery, DatabaseTable table) {
        List<DatabaseColumn> columns = removeUnnecessaryColumns(table.getColumns());
        String commaSeparatedWildcards = generateCommaSeparatedWildcards(columns.size());
        List<String> parameterNames = getColumnNamesFromColumns(columns);
        String commaSeparatedParameterNames = generateCommaSeparatedParameterNames(parameterNames);
        String queryString = String.format(
                templateQuery,
                table.getName(),
                table.getPrimaryKeyName(),
                commaSeparatedParameterNames,
                commaSeparatedWildcards);
        return new SqlQueryImpl(queryString, table.getName(), columns);
    }

    private SqlQuery generateUpdateQuery(String templateQuery, DatabaseTable table) {
        List<DatabaseColumn> columns = removeUnnecessaryColumns(table.getColumns());
        List<String> parameterNames = getColumnNamesFromColumns(columns);
        String separatedParameterNames = generateParametersWithNameString(parameterNames, false);
        String queryString = String.format(
                templateQuery,
                table.getName(),
                separatedParameterNames,
                table.getPrimaryKeyName());
        columns.add(table.getPrimaryKeyColumn()); //because sql update query template has primary key parameter
        return new SqlQueryImpl(queryString, table.getName(), columns);
    }

    /**
     * Generates string with certain number of parameters-wildcards
     * example - ?, ?, ?,
     *
     * @return string with sql parameters
     */
    private String generateCommaSeparatedWildcards(int numberOfWildcards) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfWildcards; i++) {
            sb.append(WILDCARD);
            sb.append(COMMA);
        }
        return sb.toString();
    }

    /**
     * Generates string with separated parameter names
     * example - param1, param2, param3...
     *
     * @return string with parameter names separated by comma
     */
    private String generateCommaSeparatedParameterNames(List<String> parameterNames) {
        StringBuilder sb = new StringBuilder();
        for (String parameterName : parameterNames) {
            sb.append(parameterName);
            sb.append(COMMA);
        }
        return sb.toString();
    }

    /**
     * Generates string with parameters. Gets param names from the
     * collection and adds it to generating string substituting signs '= ?'
     * and separates them via {@link #SEARCH_QUERY_PARAMETERS_SEPARATOR} or
     * {@link #COMMA}. The result is looks
     * like this: ParamName = ?, ParamName2 = ?
     * Or like this: ParamName = ? AND ParamName2 = ?
     *
     * @param isSearchQuery if true, then parameters separates by
     *                      {@link #SEARCH_QUERY_PARAMETERS_SEPARATOR},
     *                      else by
     *                      {@link #COMMA}.
     * @return String with sql parameters
     */
    private String generateParametersWithNameString(Collection<String> parametersNames, boolean isSearchQuery) {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String str : parametersNames) {
            sb.append(str);
            sb.append(" = ");
            sb.append(WILDCARD);
            if (count == parametersNames.size()) break; //if it last parameter, then not need add separator
            sb.append(isSearchQuery ? SEARCH_QUERY_PARAMETERS_SEPARATOR : COMMA);
            count++;
        }
        return sb.toString();
    }

    private List<String> getColumnNamesFromColumns(List<DatabaseColumn> columns) {
        return columns.stream().map(DatabaseColumn::getName).collect(Collectors.toList());
    }

    private List<DatabaseColumn> removeUnnecessaryColumns(List<DatabaseColumn> columns) {
        List<DatabaseColumn> unnecessaryColumns = new ArrayList<>();
        for (DatabaseColumn column : columns) {
            if (column.getName().equalsIgnoreCase(DELETED_COLUMN_NAME) || column.isPrimaryKey()) {
                unnecessaryColumns.add(column);
            }
        }
        List<DatabaseColumn> necessaryColumns = new ArrayList<>(columns);
        necessaryColumns.removeAll(unnecessaryColumns);
        return necessaryColumns;
    }

    private class SqlQueryImpl implements SqlQuery {
        private String query;
        private String tableName;
        private List<DatabaseColumn> parameters;

        public SqlQueryImpl(String query, String tableName) {
            this.query = query;
            this.tableName = tableName;
            parameters = new ArrayList<>();
        }

        private SqlQueryImpl(String query, String tableName, List<DatabaseColumn> parameters) {
            this.query = query;
            this.tableName = tableName;
            this.parameters = parameters;
        }

        public SqlQueryImpl(String query, String tableName, DatabaseColumn parameter) {
            this.query = query;
            this.tableName = tableName;
            parameters = new ArrayList<>();
            parameters.add(parameter);
        }

        @Override
        public String getQuery() {
            return query;
        }

        @Override
        public String getTableName() {
            return tableName;
        }

        @Override
        public List<DatabaseColumn> getParameters() {
            return ImmutableList.copyOf(parameters);
        }

        @Override
        public int getParameterAmount() {
            return parameters.size();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SqlQueryImpl sqlQuery = (SqlQueryImpl) o;

            if (parameters != null ? !parameters.equals(sqlQuery.parameters) : sqlQuery.parameters != null)
                return false;
            if (query != null ? !query.equals(sqlQuery.query) : sqlQuery.query != null) return false;
            if (tableName != null ? !tableName.equals(sqlQuery.tableName) : sqlQuery.tableName != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = query != null ? query.hashCode() : 0;
            result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
            result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return query;
        }
    }
}
