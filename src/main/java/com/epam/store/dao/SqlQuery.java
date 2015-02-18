package com.epam.store.dao;

import com.epam.store.metadata.DatabaseColumn;

import java.util.List;

public interface SqlQuery {
    public String getQuery();

    public String getTableName();

    public List<DatabaseColumn> getParameters();

    public int getParameterAmount();
}
