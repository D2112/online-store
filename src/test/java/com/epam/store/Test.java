package com.epam.store;

import java.sql.SQLException;

public class Test {
    private static final String USER_EMAIL_COLUMN = "EMAIL";
    private static final String ROLE_NAME_COLUMN = "NAME";
    private static final String ROLE_ID_COLUMN = "ROLE_ID";
    private static final String USER_ID_COLUMN = "USER_ID";
    private static final String DATE_TIME_COLUMN = "TIME";
    private static final String STATUS_NAME_COLUMN = "NAME";

    public static void main(String[] args) throws SQLException {
        method(2);
        method(-1);
    }

    private static void method(int i) {
        assert (i > 0) : i;
        System.out.println(i);
    }
}
