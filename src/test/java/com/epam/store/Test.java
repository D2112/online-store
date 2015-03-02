package com.epam.store;

public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test.getClass().getClassLoader().toString());
    }
}
