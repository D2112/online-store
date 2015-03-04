package com.epam.store;

import com.epam.store.model.Password;

import java.sql.SQLException;

public class Test {
    private static final String USER_EMAIL_COLUMN = "EMAIL";
    private static final String ROLE_NAME_COLUMN = "NAME";
    private static final String ROLE_ID_COLUMN = "ROLE_ID";
    private static final String USER_ID_COLUMN = "USER_ID";
    private static final String DATE_TIME_COLUMN = "TIME";
    private static final String STATUS_NAME_COLUMN = "NAME";

    public static void main(String[] args) throws SQLException {
        for (int i = 0; i < 100; i++) {
            Password encrypt = PasswordEncryptor.encrypt("12345".getBytes());
            if (encrypt.getSalt().length() != 32) {
                System.out.println("true");
            }
        }













 /*       ConnectionPool cp = new SqlConnectionPool();
        DaoFactory daoFactory = new JdbcDaoFactory(cp);
        ProductService productService = new ProductService(daoFactory);
        UserService userService = new UserService(daoFactory);

        Product product = new Product(
                "name",
                new Category("catename"),
                "descr",
                new Price(new BigDecimal("213")),
                new Image("img", "jpg", new byte[]{3, 5, 7})
                );
        productService.addProduct(product);
        userService.setUserBan(116, true);
        System.out.println();
        cp.shutdown();*/
    }
}
