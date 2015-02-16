package com.epam.store;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        Test.class.getClassLoader().loadClass("com.epam.store.Singleton");

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

