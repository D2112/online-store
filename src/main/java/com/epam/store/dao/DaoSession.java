package com.epam.store.dao;

import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.model.BaseEntity;

public interface DaoSession extends AutoCloseable {
    public <T extends BaseEntity> Dao<T> getDao(Class<T> clazz);

    public void beginTransaction();

    public void endTransaction();

    public void close();

    public SqlPooledConnection getConnection();
}
