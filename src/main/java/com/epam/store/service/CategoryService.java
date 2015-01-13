package com.epam.store.service;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private DaoFactory daoFactory;

    public CategoryService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public List<Category> getCategories() {
        List<Category> categoryList;
        try(DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            categoryList = categoryDao.findAll();
        }
        return categoryList;
    }
}
