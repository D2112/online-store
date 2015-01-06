package com.epam.store.dao;

import com.epam.store.SqlQueryManager;
import com.epam.store.SqlQueryType;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DatabaseColumn;
import com.epam.store.metadata.DatabaseTable;
import com.epam.store.metadata.EntityMetadata;
import com.epam.store.model.BaseEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JdbcDao<T extends BaseEntity> implements Dao<T> {
    private final Class<T> clazz;
    private DaoSession daoSession;
    private SqlPooledConnection connection;
    private SqlQueryManager sqlQueryManager;
    private EntityMetadata<T> entityMetadata;
    private DatabaseTable table;

    public JdbcDao(DaoSession daoSession, Class<T> clazz, SqlQueryManager sqlQueryManager, DatabaseTable table) {
        this.daoSession = daoSession;
        this.connection = daoSession.getConnection();
        this.table = table;
        this.sqlQueryManager = sqlQueryManager;
        this.clazz = clazz;
        this.entityMetadata = new EntityMetadata<>(clazz);
    }

    @Override
    public T insert(T object) {
        T insertedObject;
        String insertQuery = sqlQueryManager.getQueryForClass(SqlQueryType.INSERT, clazz);
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            prepareStatementForInsert(statement, object);
            int inserted = statement.executeUpdate();
            if (inserted > 1) throw new DaoException("Inserted more than one record: " + inserted);
            if (inserted < 1) throw new DaoException("Record was not inserted");
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        String readLastQuery = sqlQueryManager.getQueryForClass(SqlQueryType.READ_LAST, clazz);
        try (PreparedStatement statement = connection.prepareStatement(readLastQuery);
             ResultSet rs = statement.executeQuery()) {
            if (!rs.next()) throw new DaoException("Last inserted ID was not found");
            Long lastInsertedID = rs.getLong(table.getPrimaryKeyColumnName());
            object.setId(lastInsertedID);
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return object;
    }

    @Override
    public T find(long id) {
        List<T> list;
        String readQuery = sqlQueryManager.getQueryForClass(SqlQueryType.FIND_BY_ID, clazz);
        try (PreparedStatement statement = connection.prepareStatement(readQuery)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            rs.close();
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        if (list == null || list.size() == 0) {
            throw new DaoException("Record with ID = " + id + " not found.");
        }
        if (list.size() > 1) {
            throw new DaoException("Received more than one record.");
        }
        return list.get(0);
    }

    @Override
    public boolean update(T object) {
        String updateQuery = sqlQueryManager.getQueryForClass(SqlQueryType.UPDATE_BY_ID, clazz);
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            prepareStatementForUpdate(statement, object);
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
    public boolean delete(long id) {
        String deleteQuery = sqlQueryManager.getQueryForClass(SqlQueryType.DELETE_BY_ID, clazz);
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setLong(1, id);
            T entityToDelete = find(id);
            deleteDependencies(entityToDelete);
            int deleted = statement.executeUpdate();
            if (deleted > 1) {
                throw new DaoException("Deleted more than 1 record");
            }
            if (deleted < 1) {
                throw new DaoException("Record was not deleted");
            }
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return true;
    }

    @Override
    public List<T> findAll() {
        List<T> list;
        String readAllQuery = sqlQueryManager.getQueryForClass(SqlQueryType.READ_ALL, clazz);
        try (PreparedStatement statement = connection.prepareStatement(readAllQuery);
             ResultSet rs = statement.executeQuery()) {

            list = parseResultSet(rs);
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return list;
    }

    @Override
    public List<T> findByParameters(Map<String, Object> parameters) {
        List<T> list;
        String searchQuery = sqlQueryManager.getFindByParametersQuery(clazz, parameters.keySet());
        try (PreparedStatement statement = connection.prepareStatement(searchQuery)) {
            int index = 1;
            for (Object obj : parameters.values()) {
                statement.setObject(index, obj);
                index++;
            }
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            rs.close();
        } catch (SQLException exc) {
            throw new DaoException(exc);
        }
        return list;
    }

    @Override
    public List<T> findByParameter(String paramName, Object paramValue) {
        Map<String, Object> map = new HashMap<>();
        map.put(paramName, paramValue);
        return findByParameters(map);
    }

    private List<T> parseResultSet(ResultSet rs) throws SQLException {
        List<T> resultList = new ArrayList<>();
        while (rs.next()) {
            try {
                T entity = entityMetadata.getEntityClass().newInstance();
                Long id = rs.getLong(table.getPrimaryKeyColumnName());
                entity.setId(id);
                for (DatabaseColumn column : table.getColumns()) {
                    String columnName = column.getName();
                    String fieldName = column.getFieldName();
                    if (!entityMetadata.hasField(fieldName)) continue;
                    Object valueToSet;
                    if (column.isForeignKey()) {
                        Long dependencyEntityID = rs.getLong(columnName);
                        valueToSet = readDependency(fieldName, dependencyEntityID);
                    } else {
                        valueToSet = rs.getObject(columnName);
                    }
                    entityMetadata.invokeSetterByFieldName(fieldName, entity, valueToSet);
                }
                resultList.add(entity);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DaoException(e);
            }
        }
        return resultList;
    }

    private void prepareStatementForInsert(PreparedStatement statement, T entity) throws SQLException {
        int paramToSetIndex = 1;
        for (DatabaseColumn column : table.getColumns()) {
            String fieldName = column.getFieldName();
            if (!entityMetadata.hasField(fieldName)) continue;
            if (column.isForeignKey()) {
                Object dependencyEntity = entityMetadata.invokeGetterByFieldName(fieldName, entity);
                Long dependencyIdToSet = insertDependency(fieldName, dependencyEntity).getId();
                statement.setLong(paramToSetIndex, dependencyIdToSet);
            } else {
                Object valueToSet = entityMetadata.invokeGetterByFieldName(fieldName, entity);
                statement.setObject(paramToSetIndex, valueToSet);
            }
            paramToSetIndex++;
        }
    }

    private void prepareStatementForUpdate(PreparedStatement statement, T entity) throws SQLException {
        prepareStatementForInsert(statement, entity);
        int idParameterIndex = statement.getParameterMetaData().getParameterCount();
        statement.setLong(idParameterIndex, entity.getId());
    }

    private void deleteDependencies(T entity) throws SQLException {
        for (DatabaseColumn column : table.getColumns()) {
            String fieldName = column.getFieldName();
            if (!entityMetadata.hasField(fieldName)) continue;
            if (column.isForeignKey()) {
                BaseEntity entityToDelete = (BaseEntity) entityMetadata.invokeGetterByFieldName(fieldName, entity);
                Class<T> type = entityMetadata.getFieldType(fieldName);
                Dao dao = daoSession.getDao(type);
                dao.delete(entityToDelete.getId());
            }
        }
    }

    private BaseEntity readDependency(String fieldName, Long dependencyEntityID) {
        Class<T> type = entityMetadata.getFieldType(fieldName);
        Dao dao = daoSession.getDao(type);
        return dao.find(dependencyEntityID);
    }

    private BaseEntity insertDependency(String fieldName, Object entityToInsert) {
        Class<T> type = entityMetadata.getFieldType(fieldName);
        Dao dao = daoSession.getDao(type);
        BaseEntity baseEntityToInsert = (BaseEntity) entityToInsert;
        return dao.insert(baseEntityToInsert);
    }
}
