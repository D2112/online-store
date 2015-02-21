package com.epam.store;

import com.epam.store.dao.*;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.Attribute;
import com.epam.store.model.DecimalAttribute;
import com.epam.store.model.IntegerAttribute;
import com.epam.store.model.StringAttribute;

import java.math.BigDecimal;

public class AttributeDaoTest {
    public static void main(String[] args) {
        ConnectionPool cp = new SqlConnectionPool();
        SqlQueryFactory queryFactory;
        DBMetadataManager dbMetadataManager;
        try (SqlPooledConnection connection = cp.getConnection()) {
            dbMetadataManager = new DBMetadataManager(connection.getMetaData());
            queryFactory = new SqlQueryFactory(dbMetadataManager);
        }
        DaoFactory daoFactory = new JdbcDaoFactory(cp);
        Attribute intAttribute = new IntegerAttribute("int", 1);
        Attribute decimalAttribute = new DecimalAttribute("dec", new BigDecimal("2.7"));
        Attribute stringAttribute = new StringAttribute("str", "3");
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Attribute> attributeDao = daoSession.getDao(Attribute.class);
            //attributeDao.insertWithAdditionalParameter(intAttribute, "PRODUCT_ID", 1);
            //attributeDao.insertWithAdditionalParameter(decimalAttribute, "PRODUCT_ID", 1);
            //attributeDao.insertWithAdditionalParameter(stringAttribute, "PRODUCT_ID", 1);
            //List<Attribute> all =
            decimalAttribute.setId(164L);
            System.out.println(attributeDao.updateWithAdditionalParameter(decimalAttribute, "PRODUCT_ID", 1));
        }


        cp.shutdown();
    }
}
