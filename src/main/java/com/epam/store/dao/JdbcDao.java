package com.epam.store.dao;

import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DatabaseColumn;
import com.epam.store.metadata.DatabaseTable;
import com.epam.store.metadata.EntityManager;
import com.epam.store.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JdbcDao<T extends BaseEntity> implements Dao<T> {
    private static final Logger log = LoggerFactory.getLogger(JdbcDao.class);
    protected final Class<T> clazz;
    protected DaoSession daoSession;
    protected SqlPooledConnection connection;
    protected SqlQueryFactory queryFactory;
    protected EntityManager<T> entityManager;
    protected DatabaseTable table;

    public JdbcDao(DaoSession daoSession, Class<T> clazz, SqlQueryFactory queryFactory, DatabaseTable table) {
        this.daoSession = daoSession;
        this.connection = daoSession.getConnection();
        this.table = table;
        this.queryFactory = queryFactory;
        this.clazz = clazz;
        this.entityManager = EntityManager.getManager(clazz);
    }

    /**
     * {@inheritDoc}
     *
     * @throws DaoException if inserted more than one record
     *                      or if record was not inserted
     */
    @Override
    public T insert(T object) {
        return insertWithAdditionalParameters(object, new HashMap<>()); //empty parameters
    }

    @Override
    public T insertWithAdditionalParameters(T object, Map<String, Object> parameters) {
        SqlQuery insertQuery = queryFactory.getQueryForClass(SqlQueryType.INSERT, clazz);
        try (PreparedStatement statement = connection.prepareStatement(insertQuery.getQuery())) {
            prepareStatementForInsert(statement, object, insertQuery, parameters);
            int inserted = statement.executeUpdate();
            if (inserted > 1) throw new DaoException("Inserted more than one record: " + inserted);
            if (inserted < 1) throw new DaoException("Record was not inserted");
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        //getting id of the inserted object
        SqlQuery readLastQuery = queryFactory.getQueryForClass(SqlQueryType.READ_LAST, clazz);
        try (PreparedStatement statement = connection.prepareStatement(readLastQuery.getQuery());
             ResultSet rs = statement.executeQuery()) {
            if (!rs.next()) throw new DaoException("Last inserted ID was not found");
            Long lastInsertedID = rs.getLong(table.getPrimaryKeyName());
            object.setId(lastInsertedID);
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return object;
    }

    @Override
    public T insertWithAdditionalParameter(T object, String paramName, Object value) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, value);
        return insertWithAdditionalParameters(object, params);
    }

    /**
     * {@inheritDoc}
     *
     * @throws DaoException if found more than one record
     */
    @Override
    public T find(long id) {
        List<T> list;
        SqlQuery readQuery = queryFactory.getQueryForClass(SqlQueryType.FIND_BY_ID, clazz);
        try (PreparedStatement statement = connection.prepareStatement(readQuery.getQuery())) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            rs.close();
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new DaoException("Received more than one record.");
        }
        return list.get(0);
    }

    /**
     * {@inheritDoc}
     *
     * @throws DaoException if updated more than one record
     *                      or if error occurred during execute sql query
     */
    @Override
    public boolean update(T object) {
        return updateWithAdditionalParameters(object, new HashMap<>()); //empty parameters
    }

    @Override
    public boolean updateWithAdditionalParameters(T object, Map<String, Object> parameters) {
        SqlQuery updateQuery = queryFactory.getQueryForClass(SqlQueryType.UPDATE_BY_ID, clazz);
        try (PreparedStatement statement = connection.prepareStatement(updateQuery.getQuery())) {
            prepareStatementForUpdate(statement, object, updateQuery, parameters);
            int updated = statement.executeUpdate();
            if (updated > 1) {
                throw new DaoException("Updated more than one record: " + updated);
            }
            if (updated < 1) return false;
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return true;
    }

    @Override
    public boolean updateWithAdditionalParameter(T object, String paramName, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(paramName, value);
        return updateWithAdditionalParameters(object, map);
    }

    /**
     * {@inheritDoc}
     *
     * @throws DaoException if deleted more than one record
     *                      or if error occurred during execute sql query
     */
    @Override
    public boolean delete(long id) {
        SqlQuery deleteQuery = queryFactory.getQueryForClass(SqlQueryType.DELETE_BY_ID, clazz);
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery.getQuery())) {
            statement.setLong(1, id);
            int deleted = statement.executeUpdate();
            if (deleted > 1) {
                throw new DaoException("Deleted more than 1 record");
            }
            if (deleted < 1) {
                return false;
            }
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @throws DaoException if error occurred during execute sql query
     */
    @Override
    public List<T> getAll() {
        List<T> list;
        SqlQuery readAllQuery = queryFactory.getQueryForClass(SqlQueryType.READ_ALL, clazz);
        try (PreparedStatement statement = connection.prepareStatement(readAllQuery.getQuery());
             ResultSet rs = statement.executeQuery()) {

            list = parseResultSet(rs);
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     *
     * @throws DaoException if deleted more than one record
     *                      or if error occurred during execute sql query.
     */
    @Override
    public List<T> findByParameters(Map<String, Object> parameters) {
        List<T> list;
        String searchQuery = queryFactory.generateFindByParametersQuery(clazz, parameters.keySet());
        try (PreparedStatement statement = connection.prepareStatement(searchQuery)) {
            int statementParameterIndex = 1;
            for (Object obj : parameters.values()) {
                statement.setObject(statementParameterIndex, obj);
                statementParameterIndex++;
            }
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            rs.close();
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findByParameter(String paramName, Object paramValue) {
        Map<String, Object> map = new HashMap<>();
        map.put(paramName, paramValue);
        return findByParameters(map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findFirstByParameter(String paramName, Object paramValue) {
        List<T> list = findByParameter(paramName, paramValue);
        if (list.size() == 0) return null;
        return list.iterator().next();
    }

    /**
     * Sets to statement all field from the entity-parameter,
     * if entity contains other entity objects,
     * then calls specific dao of it class and read it
     * and sets to statement their id.
     *
     * @param statement to prepare
     * @param entity    to take from it values
     * @throws SQLException
     */
    protected void prepareStatementForInsert(PreparedStatement statement, T entity, SqlQuery query,
                                             Map<String, Object> additionalParameters) throws SQLException {
        int statementParameterIndex = 1;
        for (DatabaseColumn column : query.getParameters()) {
            String fieldName = column.getFieldName();
            Object valueToSet;
            if (entityManager.hasField(fieldName)) {
                valueToSet = entityManager.invokeGetter(fieldName, entity);
                if (column.isForeignKey() && valueToSet != null) {
                    //try to get id from dependency entity
                    Long dependencyID = ((BaseEntity) valueToSet).getId();
                    //if dependency object has no id, then insert object and get it's id to set to statement
                    if (dependencyID == null) {
                        dependencyID = insertDependency(fieldName, valueToSet).getId();
                    }
                    //now value it's just id of the entity
                    valueToSet = dependencyID;
                }
            } else {
                //if entity has no such field, then try to search in additional parameter by column name
                valueToSet = additionalParameters.get(column.getName());
            }
            if (valueToSet == null) continue;
            //set value like an object because it is a primitive type
            statement.setObject(statementParameterIndex, valueToSet);
            log.debug("Set to statement " + column.getName() + " from table " + table.getName());
            statementParameterIndex++;
        }
    }

    /**
     * Gets values from {@link java.sql.ResultSet} and creates objects using them
     * if entity contains other non-primitive objects,
     * then calls specific dao of it class and read it.
     *
     * @param rs is the {@link java.sql.ResultSet}
     * @return List of objects
     * @throws SQLException
     * @throws DaoException if there problems with instance
     *                      object or access to it getters or setter
     */
    protected List<T> parseResultSet(ResultSet rs) throws SQLException {
        List<T> resultList = new ArrayList<>();
        while (rs.next()) {
            try {
                T entity = entityManager.getEntityClass().newInstance(); //creating new object
                Long id = rs.getLong(table.getPrimaryKeyName()); //getting object's id from result set
                entity.setId(id);
                //for each column in table get value from rs and set it to entity if entity has such field
                for (DatabaseColumn column : table.getColumns()) {
                    String columnName = column.getName();
                    String fieldName = column.getFieldName();
                    if (!entityManager.hasField(fieldName)) continue;
                    Object valueToSet;
                    if (column.isForeignKey()) {
                        Long dependencyEntityID = rs.getLong(columnName); //getting dependency id
                        valueToSet = readDependency(fieldName, dependencyEntityID);//call another dao to read dependency
                    } else {
                        valueToSet = rs.getObject(columnName);
                    }
                    entityManager.invokeSetterByFieldName(fieldName, entity, valueToSet); //set value to entity
                }
                resultList.add(entity);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DaoException(e);
            }
        }
        return resultList;
    }

    /**
     * Sets to statement all field from the entity-parameter,
     * the main difference from
     * {@link #prepareStatementForInsert(java.sql.PreparedStatement, T, com.epam.store.dao.SqlQuery)
     * it's setting to last parameter in statement id of entity which need to be updated.
     *
     * @param statement to prepare.
     * @param entity    to take from it values.
     * @throws SQLException
     */
    private void prepareStatementForUpdate(PreparedStatement statement, T entity, SqlQuery query,
                                           Map<String, Object> additionalParameters) throws SQLException {
        prepareStatementForInsert(statement, entity, query, additionalParameters);
        //the last parameter it's id of entity which need to be updated,
        //because the sql query ends like this - ...WHERE ID = ?
        int idParameterIndex = statement.getParameterMetaData().getParameterCount();
        statement.setLong(idParameterIndex, entity.getId());
    }

    /**
     * Delete all entities which contains in entity-parameter
     * if such are exist
     *
     * @param entity which contains other entities
     * @throws SQLException
     */
    private void deleteDependencies(T entity) throws SQLException {
        for (DatabaseColumn column : table.getColumns()) {
            String fieldName = column.getFieldName();
            if (!entityManager.hasField(fieldName)) continue;
            if (column.isForeignKey()) {
                BaseEntity entityToDelete = (BaseEntity) entityManager.invokeGetter(fieldName, entity); //get entity
                Class type = entityManager.getFieldType(fieldName); //get entity type
                if (!BaseEntity.class.isAssignableFrom(type)) {
                    throw new DaoException("Trying to get dao with type which not extends BaseEntity");
                }
                Dao dao = daoSession.getDao(type); //get dao parametrized such type
                dao.delete(entityToDelete.getId());
            }
        }
    }

    /**
     * Reads entity from another table, this method needs when
     * parsing result set and there foreign keys.
     * This method gets type of dependency within metadata by field name
     * and then via DAO of this type trying to find entity
     *
     * @param fieldName field name of dependency which need to be read
     * @return Found object as {@link com.epam.store.model.BaseEntity}
     */
    private BaseEntity readDependency(String fieldName, Long dependencyEntityID) {
        Class type = entityManager.getFieldType(fieldName);
        if (!BaseEntity.class.isAssignableFrom(type)) {
            throw new DaoException("Trying to get dao with type which not extends BaseEntity");
        }
        Dao dao = daoSession.getDao(type);
        return dao.find(dependencyEntityID);
    }

    /**
     * Gets type of the entity's field and gets DAO by this type.
     * Then DAO is used to insert another entity
     *
     * @return inserted entity as {@link com.epam.store.model.BaseEntity}
     * with id from database
     */
    @SuppressWarnings("unchecked")
    private BaseEntity insertDependency(String fieldName, Object entityToInsert) {
        Class type = entityManager.getFieldType(fieldName);
        Dao dao = daoSession.getDao(type);
        if (!BaseEntity.class.isAssignableFrom(type)) {
            throw new DaoException("Trying to get DAO with type which not extends BaseEntity");
        }
        BaseEntity baseEntityToInsert = (BaseEntity) entityToInsert;
        return dao.insert(baseEntityToInsert);
    }
}
