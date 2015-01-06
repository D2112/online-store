package com.epam.store.model;

import java.math.BigDecimal;

public class Price extends BaseEntity {
    private BigDecimal value;

    public Price() {
    }

    public Price(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal price) {
        this.value = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;
        if (value != null ? !value.equals(price.value) : price.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
