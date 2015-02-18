package com.epam.store.service;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.dao.SqlQueryFactory;
import com.epam.store.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductService {
    private static final String PRODUCT_NAME_COLUMN = "NAME";
    private static final String CATEGORY_ID_COLUMN = "CATEGORY_ID";
    private DaoFactory daoFactory;
    private AttributeService attributeService;

    public ProductService(DaoFactory daoFactory, SqlQueryFactory sqlQueryFactory) {
        this.daoFactory = daoFactory;
        attributeService = new AttributeService(daoFactory, sqlQueryFactory);
    }

    public List<Product> getProductsForCategory(String categoryName) {
        List<Product> productsList = new ArrayList<>();
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            List<Category> categories = categoryDao.findByParameter(PRODUCT_NAME_COLUMN, categoryName);
            if (categories.size() == 1) {
                long categoryID = categories.get(0).getId();
                Dao<Product> productDao = daoSession.getDao(Product.class);
                productsList = productDao.findByParameter(CATEGORY_ID_COLUMN, categoryID);
                productsList.forEach(this::setAttributesToProduct);
            }
        }
        return productsList;
    }

    public Product getProductByID(long id) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            Product product = productDao.find(id);
            setAttributesToProduct(product);
            return product;
        }
    }

    public void addProduct(Product product) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            productDao.insert(product);
            attributeService.insertAttributes(product.getId(), product.getAttributes());
        }
    }

    private Product setAttributesToProduct(Product product) {
        try {
            List<Attribute> attributesForProduct = attributeService.getAttributesForProduct(product.getId());
            product.setAttributes(attributesForProduct);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
        return product;
    }

    public boolean isProductExist(String productName) {
        return getProductByName(productName) != null;
    }

    public Product getProductByName(String productName) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            List<Product> products = productDao.findByParameter(PRODUCT_NAME_COLUMN, productName);
            if (products.size() == 1) return products.iterator().next();
            return null;
        }
    }

    public void deleteProduct(long id) {
        Product product = getProductByID(id);
        attributeService.deleteAttributes(product.getAttributes());
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            daoSession.beginTransaction();
            //deleting price
            Dao<Price> priceDao = daoSession.getDao(Price.class);
            priceDao.delete(product.getPrice().getId());
            //deleting image
            Dao<Image> imageDao = daoSession.getDao(Image.class);
            imageDao.delete(product.getImage().getId());
            //deleting product
            productDao.delete(product.getId());
            daoSession.endTransaction();
        }
    }
}
