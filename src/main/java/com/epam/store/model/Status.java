package com.epam.store.model;

public class Status extends BaseEntity {
    public static final String PAID = "Paid";
    public static final String UNPAID = "Unpaid";
    public static final String CANCELED = "Canceled";
    private String name;

    public Status() {
    }

    public Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
