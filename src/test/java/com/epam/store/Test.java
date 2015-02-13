package com.epam.store;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        //Class.forName("com.epam.store.Singleton");
    }
}

class Singleton {

    static {
        System.out.println("Outer class");
    }

    public static class SingletonHolder {
        public static final Singleton HOLDER_INSTANCE = new Singleton();

        static {
            System.out.println("Holder");
        }
    }

    public static Singleton getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }
}
