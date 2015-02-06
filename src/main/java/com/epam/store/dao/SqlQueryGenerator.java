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

public class SqlQueryGenerator {
    private static final Logger log = LoggerFactory.getLogger(SqlQueryGenerator.class);
    private static final String QUERY_FILE_NAME = "query.properties";
    private static final String FIND_BY_PARAMETERS_QUERY_NAME = "FIND_BY_PARAMETERS";
    private static final String PUBLIC_TABLES_COUNT_QUERY_NAME = "PUBLIC_TABLES_COUNT";
    private Properties queries;
    private DBMetadataManager dbMetadataManager;
    private NameFormatter nameFormatter;

    public SqlQueryGenerator(DBMetadataManager dbMetadataManager) {
        this.dbMetadataManager = dbMetadataManager;
        nameFormatter = NameFormatter.getInstance();
        queries = new Properties();
        try (InputStream inputStream = SqlQueryGenerator.class.getClassLoader().getResourceAsStream(QUERY_FILE_NAME)) {
            queries.load(inputStream);
        } catch (IOException e) {
            throw new DaoException("Error reading sql query file", e);
        }
    }

    public String getQueryForClass(SqlQueryType type, Class<? extends BaseEntity> entityClass) {
        DatabaseTable databaseTable = dbMetadataManager.getTableForClass(entityClass);
        return generateQuery(type, databaseTable);
    }

    public String getFindByParametersQuery(Class<? extends BaseEntity> entityClass, Collection<String> parametersNames) {
        String tableName = nameFormatter.getTableNameForClass(entityClass);
        String templateQuery = queries.getProperty(FIND_BY_PARAMETERS_QUERY_NAME);
        String templateWithTableName = String.format(templateQuery, tableName, "%s");
        return String.format(templateWithTableName, generateParametersWithNameString(parametersNames, true));
    }

    public String getFindByParameterQuery(Class<? extends BaseEntity> entityClass, String parameterName) {
        List<String> parameters = new ArrayList<>();
        parameters.add(parameterName);
        return getFindByParametersQuery(entityClass, parameters);
    }

    public String getPublicTablesCountQuery() {
        return queries.getProperty(PUBLIC_TABLES_COUNT_QUERY_NAME);
    }

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

    private String generateParametersString(int numberOfParameters) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfParameters; i++) {
            sb.append("?, ");
        }
        return sb.toString();
    }

    private String generateParametersWithNameString(Collection<String> parametersNames, boolean isSearchQuery) {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String str : parametersNames) {
            sb.append(str);
            sb.append(" = ?");
            if (count == parametersNames.size()) break;
            sb.append(isSearchQuery ? " AND " : ", ");
            count++;
        }
        return sb.toString();
    }

    private String generateParametersWithNameString(DatabaseTable table, boolean isSearchQuery) {
        List<String> parameters = new ArrayList<>();
        for (DatabaseColumn column : table.getColumns()) {
            parameters.add(column.getName());
        }
        return generateParametersWithNameString(parameters, isSearchQuery);
    }
}
