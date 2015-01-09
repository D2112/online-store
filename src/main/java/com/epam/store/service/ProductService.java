package com.epam.store.service;

import com.epam.store.dao.SqlQueryGenerator;
import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Attribute;
import com.epam.store.model.Product;

import java.sql.SQLException;
import java.util.List;


public class ProductService {
    private DaoFactory daoFactory;
    private SqlQueryGenerator sqlQueryGenerator;
    private AttributeService attributeService;

    public ProductService(DaoFactory daoFactory, SqlQueryGenerator sqlQueryGenerator) {
        this.sqlQueryGenerator = sqlQueryGenerator;
        this.daoFactory = daoFactory;
        attributeService = new AttributeService(daoFactory, sqlQueryGenerator);
    }

    public List<Product> getProductsForCategory(String category) throws SQLException {
        List<Product> productsList;
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            productsList = productDao.findByParameter("Category", category);
            for (Product product : productsList) {
                List<Attribute> attributesForProduct = attributeService.getAttributesForProduct(product.getId());
                product.setAttributes(attributesForProduct);
            }
        }
        return productsList;
    }

    public void addProduct(Product product) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            productDao.insert(product);
            attributeService.insertAttributes(product.getId(), product.getAttributes());
        }
    }
}
