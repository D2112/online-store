package com.epam.store.service;

import com.epam.store.dao.*;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.EntityMetadata;
import com.epam.store.metadata.NameFormatter;
import com.epam.store.model.Attribute;
import com.epam.store.model.DecimalAttribute;
import com.epam.store.model.IntegerAttribute;
import com.epam.store.model.StringAttribute;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class AttributeService {
    private static final String ATTRIBUTE_ID_COLUMN = "ATTRIBUTE_ID";
    private static final String PRODUCT_ID_COLUMN = "PRODUCT_ID";
    private static final String ATTRIBUTE_NAME_COLUMN = "name";
    private static final String ATTRIBUTE_VALUE_COLUMN = "value";
    private DaoFactory daoFactory;
    private SqlQueryGenerator sqlQueryGenerator;
    private List<Class> attributeClasses;

    public AttributeService(DaoFactory daoFactory, SqlQueryGenerator sqlQueryGenerator) {
        this.sqlQueryGenerator = sqlQueryGenerator;
        this.daoFactory = daoFactory;

        attributeClasses = new ArrayList<>();
        attributeClasses.add(StringAttribute.class);
        attributeClasses.add(IntegerAttribute.class);
        attributeClasses.add(DecimalAttribute.class);
    }

    @SuppressWarnings("unchecked")
    public List<Attribute> getAttributesForProduct(long productID) throws SQLException {
        List<Attribute> attributeList = new ArrayList<>();
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            //get attributes of every classes
            for (Class attributeClass : attributeClasses) {
                String sqlQuery = sqlQueryGenerator.generateFindByParameterQuery(attributeClass, PRODUCT_ID_COLUMN);
                List<Attribute> attributesOfCertainClass =
                        getAttributesOfCertainClass(productID, sqlQuery, attributeClass, daoSession.getConnection());
                attributeList.addAll(attributesOfCertainClass);
            }
        }
        return attributeList;
    }

    public void deleteAttributes(List<Attribute> attributeList) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            for (Attribute attribute : attributeList) {
                Dao<? extends Attribute> dao = daoSession.getDao(attribute.getClass());
                dao.delete(attribute.getId());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void insertAttributes(long productID, List<Attribute> attributeList) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            SqlPooledConnection connection = daoSession.getConnection();
            Dao<Attribute> attributeDao = daoSession.getDao(Attribute.class);
            for (Attribute attribute : attributeList) {
                //try to find attribute with the same name because attribute name must be unique
                Long attributeID = readAttributeID(attribute.getName(), connection);
                //if the attribute not found insert new one and get ID from inserted
                if (attributeID == null) {
                    attributeID = attributeDao.insert(attribute).getId();
                }
                //inserting attribute
                String insertAttributeQuery = sqlQueryGenerator.generateQueryForClass(SqlQueryType.INSERT, attribute.getClass());
                //metadata needs for getting value from one of several attribute class
                //because every class has different types of value
                EntityMetadata attributeMetadata = new EntityMetadata(attribute.getClass());
                try (PreparedStatement statement = connection.prepareStatement(insertAttributeQuery)) {
                    statement.setLong(1, attributeID);
                    statement.setLong(2, productID);
                    statement.setObject(3, attributeMetadata.invokeGetter(ATTRIBUTE_VALUE_COLUMN, attribute)); //attribute value
                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new ServiceException(e);
                }
            }
        }
    }

    private <T extends Attribute> List<Attribute> getAttributesOfCertainClass(
            long productID, String query, Class<T> clazz, SqlPooledConnection connection) {

        List<Attribute> attributeList = new ArrayList<>();
        EntityMetadata<T> attributeEntityMetadata = new EntityMetadata<>(clazz);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, productID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Attribute attribute = clazz.newInstance();
                String attributePrimaryKey = getPrimaryKeyNameForAttributeClass(clazz);
                Long abstractAttributeID = rs.getLong(ATTRIBUTE_ID_COLUMN); //abstract attribute need for getting name
                Long certainAttributeID = rs.getLong(attributePrimaryKey);
                attribute.setId(certainAttributeID);
                String attributeName = readAttributeName(abstractAttributeID, connection); //get attribute name by id
                Object value = rs.getObject(ATTRIBUTE_VALUE_COLUMN);
                attributeEntityMetadata.invokeSetterByFieldName(ATTRIBUTE_NAME_COLUMN, attribute, attributeName);
                attributeEntityMetadata.invokeSetterByFieldName(ATTRIBUTE_VALUE_COLUMN, attribute, value);
                attributeList.add(attribute);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new ServiceException(e);
        }
        return attributeList;
    }

    private String readAttributeName(long attributeID, SqlPooledConnection connection) {
        String sqlQuery = sqlQueryGenerator.generateQueryForClass(SqlQueryType.FIND_BY_ID, Attribute.class);
        String attributeName = null;
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setLong(1, attributeID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                attributeName = rs.getString(ATTRIBUTE_NAME_COLUMN);
            }
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
        return attributeName;
    }

    private Long readAttributeID(String attributeName, SqlPooledConnection connection) {
        String findAttributeByNameQuery = sqlQueryGenerator.generateFindByParameterQuery(Attribute.class, ATTRIBUTE_NAME_COLUMN);
        try (PreparedStatement statement = connection.prepareStatement(findAttributeByNameQuery)) {
            statement.setString(1, attributeName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
        return null; //not found
    }

    private String getPrimaryKeyNameForAttributeClass(Class<? extends Attribute> clazz) {
        String tableNameForClass = NameFormatter.getTableNameForClass(clazz);
        return NameFormatter.getPrimaryKeyNameForTable(tableNameForClass);
    }
}
