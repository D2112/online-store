package com.epam.store.dao;

import com.epam.store.model.BaseEntity;

import java.util.List;
import java.util.Map;

public interface Dao<T extends BaseEntity> {
    public T insert(T object);

    public T find(long id);

    public boolean update(T object);

    public boolean delete(long id);

    public List<T> findAll();

    public List<T> findByParameter(String paramName, Object paramValue);

    public List<T> findByParameters(Map<String, Object> parameters);
}
