package com.epam.store;

import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.dao.JdbcDaoFactory;
import com.epam.store.dao.SqlQueryGenerator;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.*;
import com.epam.store.service.CategoryService;
import com.epam.store.service.ProductService;
import org.joda.money.BigMoney;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


public class Test {
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws SQLException {
        ConnectionPool connectionPool = new SqlConnectionPool();
        SqlPooledConnection connection = connectionPool.getConnection();
        DBMetadataManager dbMetadataManager = new DBMetadataManager(connection.getMetaData());
        SqlQueryGenerator sqlQueryGenerator = new SqlQueryGenerator(dbMetadataManager);

        DaoFactory daoFactory = new JdbcDaoFactory(connectionPool, sqlQueryGenerator);
        DaoSession daoSession = daoFactory.getDaoSession();

        Product bread = new Product();
        bread.setName("Pitt's bread");
        bread.setCategory(new Category("Food"));
        bread.setPrice(new Price(new BigDecimal("43")));

        List<Attribute> attributeList = new ArrayList<>();
        StringAttribute stringAttribute = new StringAttribute("high");
        stringAttribute.setName("damage resistance");
        attributeList.add(stringAttribute);

        bread.setAttributes(attributeList);

        ProductService productService = new ProductService(daoFactory, sqlQueryGenerator);
        productService.addProduct(bread);

        List<Product> food = productService.getProductsForCategory("food");
        for (Product product : food) {
            System.out.println(product);
        }
    }
}
