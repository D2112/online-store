package com.epam.store.model;

public abstract class Attribute extends BaseEntity implements Comparable<Attribute> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getValueAsString();

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                "} ";
    }
}
