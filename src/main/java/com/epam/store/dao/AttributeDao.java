package com.epam.store.dao;

import com.epam.store.metadata.DatabaseTable;
import com.epam.store.metadata.EntityManager;
import com.epam.store.model.Attribute;
import com.epam.store.model.DecimalAttribute;
import com.epam.store.model.IntegerAttribute;
import com.epam.store.model.StringAttribute;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DaoClass(entityClasses = {Attribute.class, IntegerAttribute.class, StringAttribute.class, DecimalAttribute.class,})
public class AttributeDao extends JdbcDao<Attribute> {
    private static final String VALUE_FIELD_NAME = "value";

    public AttributeDao(DaoSession daoSession, Class<Attribute> clazz, SqlQueryFactory queryFactory, DatabaseTable table) {
        super(daoSession, clazz, queryFactory, table);
    }


    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Attribute entity, SqlQuery query,
                                             Map<String, Object> additionalParameters) throws SQLException {

        EntityManager entityManager = EntityManager.getManager(entity.getClass());
        Object valueFromEntity = entityManager.invokeGetter(VALUE_FIELD_NAME, entity);
        String attributeClassName = entity.getClass().getSimpleName();
        Object integerValue = null;
        Object decimalValue = null;
        Object stringValue = null;
        if (attributeClassName.equals(AttributeClass.INTEGER.name)) integerValue = valueFromEntity;
        if (attributeClassName.equals(AttributeClass.DECIMAL.name)) decimalValue = valueFromEntity;
        if (attributeClassName.equals(AttributeClass.STRING.name)) stringValue = valueFromEntity;
        //product id gets from additional parameters
        statement.setObject(Column.PRODUCT_ID.index, additionalParameters.get(Column.PRODUCT_ID.name()));
        statement.setString(Column.NAME.index, entity.getName());
        statement.setObject(Column.INTEGER_VALUE.index, integerValue);
        statement.setObject(Column.DECIMAL_VALUE.index, decimalValue);
        statement.setObject(Column.STRING_VALUE.index, stringValue);
    }

    protected List<Attribute> parseResultSet(ResultSet rs) throws SQLException {
        List<Attribute> attributeResultList = new ArrayList<>();
        while (rs.next()) {
            long id = rs.getLong(table.getPrimaryKeyName());
            String name = rs.getString(Column.NAME.name());
            Integer integerValue = (Integer) rs.getObject(Column.INTEGER_VALUE.name()); //need to get null instead 0
            BigDecimal decimalValue = rs.getBigDecimal(Column.DECIMAL_VALUE.name());
            String stringValue = rs.getString(Column.STRING_VALUE.name());
            Attribute attribute = null;
            if (integerValue != null) {
                attribute = new IntegerAttribute(name, integerValue);
            } else if (decimalValue != null) {
                attribute = new DecimalAttribute(name, decimalValue);
            } else if (stringValue != null) {
                attribute = new StringAttribute(name, stringValue);
            }
            if (attribute != null) {
                attribute.setId(id);
                attributeResultList.add(attribute);
            }
        }
        return attributeResultList;
    }

    private enum Column {
        PRODUCT_ID(1),
        NAME(2),
        INTEGER_VALUE(3),
        DECIMAL_VALUE(4),
        STRING_VALUE(5);

        int index;

        Column(int index) {
            this.index = index;
        }
    }

    private enum AttributeClass {
        INTEGER(IntegerAttribute.class.getSimpleName()),
        DECIMAL(DecimalAttribute.class.getSimpleName()),
        STRING(StringAttribute.class.getSimpleName());

        String name;

        AttributeClass(String classSimpleName) {
            name = classSimpleName;
        }
    }
}
