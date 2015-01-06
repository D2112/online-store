package com.epam.store.model;

public class StringAttribute extends Attribute {
    private String value;

    public StringAttribute() {
    }

    public StringAttribute(String value) {
        this.value = value;
    }

    @Override
    public String getValueAsString() {
        return getValue();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(Attribute o) {
        return 0;
    }

    @Override
    public String toString() {
        return "StringAttribute{" +
                "value='" + value + '\'' +
                "} " + super.toString();
    }
}
