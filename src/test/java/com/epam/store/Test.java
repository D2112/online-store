package com.epam.store;

import com.epam.store.dao.SqlQuery;
import com.epam.store.dao.SqlQueryFactory;
import com.epam.store.dao.SqlQueryType;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.metadata.DatabaseColumn;
import com.epam.store.model.Product;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        ConnectionPool cp = new SqlConnectionPool();
        DBMetadataManager dbMetadataManager;
        SqlQueryFactory queryFactory;
        try (SqlPooledConnection connection = cp.getConnection()) {
            queryFactory = new SqlQueryFactory(connection.getMetaData());
        }
        for (SqlQueryType type : SqlQueryType.values()) {
            SqlQuery query = queryFactory.getQueryForClass(type, Product.class);
            for (DatabaseColumn column : query.getParameters()) {
                System.out.print(column.getName());
                System.out.print(", ");
            }
            System.out.println();
        }
        cp.shutdown();
    }
}

