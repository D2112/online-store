package com.epam.store.model;

public class IntAttribute extends Attribute {
    private Integer value;

    public IntAttribute() {
    }

    public IntAttribute(Integer value) {
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
            IntAttribute other = (IntAttribute) o;
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
