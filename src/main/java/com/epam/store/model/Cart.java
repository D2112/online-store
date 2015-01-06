package com.epam.store.model;

import java.util.ArrayList;
import java.util.List;

public class Cart extends BaseEntity {
    private List<Product> products;

    public Cart() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProduct(Product product) {
        return products.remove(product);
    }

    public int productAmount() {
        return products.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;

        if (products != null ? !products.equals(cart.products) : cart.products != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return products != null ? products.hashCode() : 0;
    }
}
