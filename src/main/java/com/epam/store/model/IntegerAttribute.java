package com.epam.store.model;

public class IntegerAttribute extends Attribute {
    private Integer value;

    public IntegerAttribute() {
    }

    public IntegerAttribute(String name, Integer value) {
        super(name);
        this.value = value;
    }

    @Override
    public String getValueAsString() {
        return String.valueOf(value);
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public int compareTo(Attribute o) {
        if (this.getClass() == o.getClass()) {
            IntegerAttribute other = (IntegerAttribute) o;
            return Integer.compare(this.getValue(), other.getValue());
        }
        if (o.getClass() == DecimalAttribute.class) {
            DecimalAttribute other = (DecimalAttribute) o;
            int otherValue = other.getValue().intValue();
            return Integer.compare(this.getValue(), otherValue);
        }
        return 1;
    }

    @Override
    public String toString() {
        return "IntAttribute{" +
                "value=" + value +
                "} " + super.toString();
    }
}
