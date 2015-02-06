package com.epam.store.model;

import java.math.BigDecimal;

public class DecimalAttribute extends Attribute {
    private BigDecimal value;

    public DecimalAttribute() {
    }

    public DecimalAttribute(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String getValueAsString() {
        return value.toString();
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public int compareTo(Attribute o) {
        if (this.getClass() == o.getClass()) {
            DecimalAttribute other = (DecimalAttribute) o;
            return other.getValue().compareTo(this.getValue());
        }
        if (o.getClass() == IntegerAttribute.class) {
            IntegerAttribute other = (IntegerAttribute) o;
            int otherValue = other.getValue();
            return Integer.compare(this.getValue().intValue(), otherValue);
        }
        return 1;
    }

    @Override
    public String toString() {
        return "DecimalAttribute{" +
                "value=" + value +
                "} " + super.toString();
    }
}
