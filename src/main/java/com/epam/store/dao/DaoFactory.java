package com.epam.store.dao;

public interface DaoFactory {
    public abstract DaoSession getDaoSession();
}
