package com.epam.store;

import com.epam.store.metadata.NameFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


public class Test {
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws SQLException {
        System.out.println(NameFormatter.getInstance().getFieldNameFromColumnName("ROLE_NAME"));
    }
}
