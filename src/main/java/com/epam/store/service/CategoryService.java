package com.epam.store.service;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Category;

import java.util.List;

public class CategoryService {
    private DaoFactory daoFactory;

    public CategoryService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public List<Category> getCategories() {
        List<Category> categoryList;
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            categoryList = categoryDao.getAll();
            return categoryList;
        }
    }

    public Category addCategory(String categoryName) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            //check if there exist the same category
            Category categoryFromDatabase = categoryDao.findFirstByParameter("name", categoryName);
            if (categoryFromDatabase != null) return null;
            Category category = new Category(categoryName);
            return categoryDao.insert(category);
        }
    }

    public boolean deleteCategory(Category category) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            Long id = category.getId();
            return id != null && categoryDao.delete(id);
        }
    }

    public Category getCategory(String categoryName) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            return categoryDao.findFirstByParameter("name", categoryName);
        }
    }
}
