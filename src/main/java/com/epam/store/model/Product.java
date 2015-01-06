package com.epam.store.model;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Product extends BaseEntity {
    private String name;
    private String category;
    private Price price;
    private List<Attribute> attributes;

    public Product() {
        attributes = new ArrayList<>();
    }

    public Product(String name, String category, Price price) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Attribute> getAttributes() {
        return ImmutableList.copyOf(attributes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Product product = (Product) o;

        if (attributes != null ? !attributes.equals(product.attributes) : product.attributes != null) return false;
        if (category != null ? !category.equals(product.category) : product.category != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", attributes=" + attributes +
                '}';
    }
}
