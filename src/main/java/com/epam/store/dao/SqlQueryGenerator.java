package com.epam.store.dao;

import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.metadata.DatabaseColumn;
import com.epam.store.metadata.DatabaseTable;
import com.epam.store.metadata.NameFormatter;
import com.epam.store.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Format SQL query templates for different types of query
 * defined in {@link com.epam.store.dao.SqlQueryType}.
 * For work needs file {@link #QUERY_FILE_NAME} with query templates
 */
public class SqlQueryGenerator {
    private static final Logger log = LoggerFactory.getLogger(SqlQueryGenerator.class);
    private static final String QUERY_FILE_NAME = "query.properties";
    private static final String FIND_BY_PARAMETERS_QUERY_NAME = "FIND_BY_PARAMETERS";
    private static final String PUBLIC_TABLES_COUNT_QUERY_NAME = "PUBLIC_TABLES_COUNT";
    private static final String SEARCH_QUERY_PARAMETERS_SEPARATOR = "AND";
    private static final String WILDCARD_PARAMETERS_QUERY_SEPARATOR = ", ";
    private Properties queries;
    private DBMetadataManager dbMetadataManager;

    public SqlQueryGenerator(DBMetadataManager dbMetadataManager) {
        this.dbMetadataManager = dbMetadataManager;
        queries = new Properties();
        try (InputStream inputStream = SqlQueryGenerator.class.getClassLoader().getResourceAsStream(QUERY_FILE_NAME)) {
            queries.load(inputStream);
        } catch (IOException e) {
            throw new DaoException("Error reading sql query file", e);
        }
    }

    /**
     * Generates query of specified type,
     * the table name in query shall be taken from class.
     *
     * @param type        of query
     * @param entityClass - some entity class which collates with database table
     *                    needs to take table name from class name
     * @return Parametrized SQL query for Prepared Statement
     */
    public String generateQueryForClass(SqlQueryType type, Class<? extends BaseEntity> entityClass) {
        DatabaseTable databaseTable = dbMetadataManager.getTableForClass(entityClass);
        return generateQuery(type, databaseTable);
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
                String.format(templateQuery, tableName, "%s"); //substitute only table name for now
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
     * depends on passed {@link com.epam.store.dao.SqlQueryType} parameter.
     *
     * @param table needs for data about columns for create query
     * @return completely finished sql query with wildcards(?) parameters
     * or without, depends on query type
     */
    private String generateQuery(SqlQueryType type, DatabaseTable table) {
        List<DatabaseColumn> columns = table.getColumns();
        String templateQuery = queries.getProperty(type.name());
        String templateWithTableName = String.format(templateQuery, table.getName(), "%s");
        String query = templateWithTableName;
        if (type == SqlQueryType.UPDATE_BY_ID) {
            query = String.format(templateWithTableName, generateParametersWithNameString(table, false));
        }
        if (type == SqlQueryType.INSERT) {
            query = String.format(templateWithTableName, generateParametersString(columns.size()));
        }
        log.debug("Generated query " + type + ": " + query);
        return query;
    }

    /**
     * Generates string with certain number of parameters-wildcards
     * example - ?, ?, ?,
     *
     * @return string with sql parameters
     */
    private String generateParametersString(int numberOfParameters) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfParameters; i++) {
            sb.append("?, ");
        }
        return sb.toString();
    }

    /**
     * Generates string with parameters. Gets param names from the
     * collection and adds it to generating string substituting signs '= ?'
     * and separates them via {@link #SEARCH_QUERY_PARAMETERS_SEPARATOR} or
     * {@link #WILDCARD_PARAMETERS_QUERY_SEPARATOR}. The result is looks
     * like this: ParamName = ?, ParamName2 = ?
     * Or like this: ParamName = ? AND ParamName2 = ?
     *
     * @param isSearchQuery if true, then parameters separates by
     *                      {@link #SEARCH_QUERY_PARAMETERS_SEPARATOR},
     *                      else by
     *                      {@link #WILDCARD_PARAMETERS_QUERY_SEPARATOR}.
     * @return String with sql parameters
     */
    private String generateParametersWithNameString(Collection<String> parametersNames, boolean isSearchQuery) {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String str : parametersNames) {
            sb.append(str);
            sb.append(" = ?");
            if (count == parametersNames.size()) break; //if it last parameter, then not need add separator
            sb.append(isSearchQuery ? SEARCH_QUERY_PARAMETERS_SEPARATOR : WILDCARD_PARAMETERS_QUERY_SEPARATOR);
            count++;
        }
        return sb.toString();
    }

    /**
     * The same method as
     * {@link #generateParametersWithNameString(Collection, boolean)}
     * The difference is what instead passing collection with parameters
     * here parameters names takes from
     * #{@link com.epam.store.metadata.DatabaseTable}
     */
    private String generateParametersWithNameString(DatabaseTable table, boolean isSearchQuery) {
        List<String> parameters = new ArrayList<>();
        for (DatabaseColumn column : table.getColumns()) {
            parameters.add(column.getName());
        }
        return generateParametersWithNameString(parameters, isSearchQuery);
    }
}
