package com.epam.store;

import java.sql.ResultSet;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.epam.store.Singleton");
        ResultSet resultSet;


    }
}

class Singleton {
    private static Singleton instance = new Singleton();

    static {
        System.out.println("static");
    }

    public static Singleton getInstance() {
        return instance;
    }
}

