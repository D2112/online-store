package com.epam.store.service;

import com.epam.store.dao.*;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.EntityMetadata;
import com.epam.store.model.Attribute;
import com.epam.store.model.DecimalAttribute;
import com.epam.store.model.IntAttribute;
import com.epam.store.model.StringAttribute;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class AttributeService {
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_VALUE = "value";
    private DaoFactory daoFactory;
    private SqlQueryGenerator sqlQueryGenerator;
    private List<Class> attributeClasses;

    public AttributeService(DaoFactory daoFactory, SqlQueryGenerator sqlQueryGenerator) {
        this.sqlQueryGenerator = sqlQueryGenerator;
        this.daoFactory = daoFactory;

        attributeClasses = new ArrayList<>();
        attributeClasses.add(StringAttribute.class);
        attributeClasses.add(IntAttribute.class);
        attributeClasses.add(DecimalAttribute.class);
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
                String insertAttributeQuery = sqlQueryGenerator.getQueryForClass(SqlQueryType.INSERT, attribute.getClass());
                //metadata needs for getting value from one of several attribute class
                //because every class has different types of value
                EntityMetadata attributeMetadata = new EntityMetadata(attribute.getClass());
                try (PreparedStatement statement = connection.prepareStatement(insertAttributeQuery)) {
                    statement.setLong(1, attributeID);
                    statement.setLong(2, productID);
                    statement.setObject(3, attributeMetadata.invokeGetter(ATTRIBUTE_VALUE, attribute));
                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new ServiceException(e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Attribute> getAttributesForProduct(long productID) throws SQLException {
        List<Attribute> attributeList = new ArrayList<>();
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            for (Class attributeClass : attributeClasses) {
                String sqlQuery = sqlQueryGenerator.getFindByParameterQuery(attributeClass, "PRODUCT_ID");
                List<Attribute> attributesOfCertainClass =
                        getAttributesOfCertainClass(productID, sqlQuery, attributeClass, daoSession.getConnection());
                attributeList.addAll(attributesOfCertainClass);
            }
        }
        return attributeList;
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
                Long attributeID = rs.getLong("ATTRIBUTE_ID");
                String attributeName = readAttributeName(attributeID, connection);
                Object value = rs.getObject("VALUE");
                attributeEntityMetadata.invokeSetterByFieldName(ATTRIBUTE_NAME, attribute, attributeName);
                attributeEntityMetadata.invokeSetterByFieldName(ATTRIBUTE_VALUE, attribute, value);
                attributeList.add(attribute);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new ServiceException(e);
        }
        return attributeList;
    }

    private String readAttributeName(long attributeID, SqlPooledConnection connection) {
        String sqlQuery = sqlQueryGenerator.getQueryForClass(SqlQueryType.FIND_BY_ID, Attribute.class);
        String attributeName = null;
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setLong(1, attributeID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                attributeName = rs.getString("NAME");
            }
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
        return attributeName;
    }

    private Long readAttributeID(String attributeName, SqlPooledConnection connection) {
        String findAttributeByNameQuery = sqlQueryGenerator.getFindByParameterQuery(Attribute.class, ATTRIBUTE_NAME);
        try (PreparedStatement statement = connection.prepareStatement(findAttributeByNameQuery)) {
            statement.setString(1, attributeName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
        return null;
    }
}
