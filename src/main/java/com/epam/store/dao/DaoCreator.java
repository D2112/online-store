package com.epam.store.dao;

import com.epam.store.metadata.DatabaseTable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Class which creates dao object with a reflection
 * Need for lazy initialization of dao classes in {@link com.epam.store.dao.JdbcDaoFactory}
 */
class DaoCreator {
    private Class<?> daoClazz;

    public DaoCreator(Class<?> daoClazz) {
        this.daoClazz = daoClazz;
    }

    public Dao create(DaoSession daoSession, Class clazz, SqlQueryFactory sqlQueryFactory, DatabaseTable table) {
        try {
            Constructor<?> constructor =
                    daoClazz.getConstructor(DaoSession.class, Class.class, SqlQueryFactory.class, DatabaseTable.class);
            return (Dao) constructor.newInstance(daoSession, clazz, sqlQueryFactory, table);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new DaoException(e);
        }
    }
}