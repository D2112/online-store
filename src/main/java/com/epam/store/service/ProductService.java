package com.epam.store.service;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.dao.SqlQueryGenerator;
import com.epam.store.model.Attribute;
import com.epam.store.model.Category;
import com.epam.store.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductService {
    private DaoFactory daoFactory;
    private AttributeService attributeService;

    public ProductService(DaoFactory daoFactory, SqlQueryGenerator sqlQueryGenerator) {
        this.daoFactory = daoFactory;
        attributeService = new AttributeService(daoFactory, sqlQueryGenerator);
    }

    public List<Product> getProductsForCategory(String categoryName) {
        List<Product> productsList = new ArrayList<>();
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            List<Category> categories = categoryDao.findByParameter("name", categoryName);
            if (categories.size() == 1) {
                long categoryID = categories.get(0).getId();
                Dao<Product> productDao = daoSession.getDao(Product.class);
                productsList = productDao.findByParameter("Category_ID", categoryID);
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

    public Product getProductByName(String productName) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            List<Product> products = productDao.findByParameter("name", productName);
            if (products.size() == 1) return products.iterator().next();
            return null;
        }
    }

    public void deleteProduct(long id) {
        Product product = getProductByID(id);
        attributeService.deleteAttributes(product.getAttributes());
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
/*            //deleting price
            Dao<Price> priceDao = daoSession.getDao(Price.class);
            priceDao.delete(product.getPrice().getId());
            //deleting image
            Dao<Image> imageDao = daoSession.getDao(Image.class);
            imageDao.delete(product.getImage().getId());*/
            //deleting product
            Dao<Product> productDao = daoSession.getDao(Product.class);
            productDao.delete(product.getId());
        }
    }
}
