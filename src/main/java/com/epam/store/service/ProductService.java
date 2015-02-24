package com.epam.store.service;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.*;

import java.util.ArrayList;
import java.util.List;


public class ProductService {
    private static final String PRODUCT_NAME_COLUMN = "NAME";
    private static final String PRODUCT_ID_COLUMN = "PRODUCT_ID";
    private static final String CATEGORY_ID_COLUMN = "CATEGORY_ID";
    private DaoFactory daoFactory;

    public ProductService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public List<Product> getProductsForCategory(String categoryName) {
        List<Product> productsList = new ArrayList<>();
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            Category category = categoryDao.findFirstByParameter(PRODUCT_NAME_COLUMN, categoryName);
            if (category != null) {
                Dao<Product> productDao = daoSession.getDao(Product.class);
                Dao<Attribute> attributeDao = daoSession.getDao(Attribute.class);
                productsList = productDao.findByParameter(CATEGORY_ID_COLUMN, category.getId());
                for (Product product : productsList) {
                    List<Attribute> attributeList = attributeDao.findByParameter(PRODUCT_ID_COLUMN, product.getId());
                    product.setAttributes(attributeList);
                }
            }
        }
        return productsList;
    }

    public Product getProductByID(long id) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            Dao<Attribute> attributeDao = daoSession.getDao(Attribute.class);
            Product product = productDao.find(id);
            if (product != null) {
                List<Attribute> attributeList = attributeDao.findByParameter(PRODUCT_ID_COLUMN, product.getId());
                product.setAttributes(attributeList);
            }
            return product;
        }
    }


    public void addProduct(Product product) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            daoSession.beginTransaction();
            productDao.insert(product);
            insertAttributes(daoSession, product.getAttributes(), product.getId());
            daoSession.endTransaction();
        }
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
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            Dao<Price> priceDao = daoSession.getDao(Price.class);
            Dao<Image> imageDao = daoSession.getDao(Image.class);
            daoSession.beginTransaction();
            deleteAttributes(daoSession, product.getAttributes());
            priceDao.delete(product.getPrice().getId()); //deleting price
            imageDao.delete(product.getImage().getId()); //deleting image
            productDao.delete(product.getId()); //deleting product
            daoSession.endTransaction();
        }
    }


    private void insertAttributes(DaoSession daoSession, List<Attribute> attributes, long productID) {
        Dao<Attribute> attributeDao = daoSession.getDao(Attribute.class);
        for (Attribute attribute : attributes) {
            attributeDao.insertWithAdditionalParameter(attribute, PRODUCT_ID_COLUMN, productID);
        }
    }

    private void deleteAttributes(DaoSession daoSession, List<Attribute> attributes) {
        Dao<Attribute> attributeDao = daoSession.getDao(Attribute.class);
        for (Attribute attribute : attributes) {
            attributeDao.delete(attribute.getId());
        }
    }
}
