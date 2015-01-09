package com.epam.store.service;

import com.epam.store.dao.SqlQueryGenerator;
import com.epam.store.dao.SqlQueryType;
import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
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
            daoSession.beginTransaction();
            Dao<Attribute> attributeDao = daoSession.getDao(Attribute.class);
            for (Attribute attribute : attributeList) {
                EntityMetadata attributeMetadata = new EntityMetadata(attribute.getClass());
                Attribute inserted = attributeDao.insert(attribute);
                String insertAttributeQuery = sqlQueryGenerator.getQueryForClass(SqlQueryType.INSERT, attribute.getClass());
                long attributeID = inserted.getId();
                try (PreparedStatement statement = connection.prepareStatement(insertAttributeQuery)) {
                    statement.setLong(1, attributeID);
                    statement.setLong(2, productID);
                    statement.setObject(3, attributeMetadata.invokeGetterByFieldName("value", attribute));
                    int insertedAmount = statement.executeUpdate();
                } catch (SQLException e) {
                    throw new ServiceException(e);
                }
            }
            daoSession.endTransaction();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Attribute> getAttributesForProduct(long productID) throws SQLException {
        List<Attribute> attributeList = new ArrayList<>();
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            for (Class attributeClass : attributeClasses) {
                String sqlQuery = sqlQueryGenerator.getFindByParameterQuery(attributeClass, "PRODUCT_ID");
                List attributesOfConcreteClass = getAttributesOfConcreteClass(productID, sqlQuery, attributeClass, daoSession.getConnection());
                attributeList.addAll(attributesOfConcreteClass);
            }
        }
        return attributeList;
    }

    private <T extends Attribute> List<Attribute> getAttributesOfConcreteClass(
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
                attributeEntityMetadata.invokeSetterByFieldName("name", attribute, attributeName);
                attributeEntityMetadata.invokeSetterByFieldName("value", attribute, value);
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
}
