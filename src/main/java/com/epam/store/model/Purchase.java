package com.epam.store.model;

import java.sql.Date;

public class Purchase extends BaseEntity {
    private Product product;
    private Price price;
    private Date date;

    public Purchase() {

    }

    public Purchase(Product product, Price price, Date date) {
        this.product = product;
        this.price = price;
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Purchase purchase = (Purchase) o;

        if (date != null ? !date.equals(purchase.date) : purchase.date != null) return false;
        if (price != null ? !price.equals(purchase.price) : purchase.price != null) return false;
        if (product != null ? !product.equals(purchase.product) : purchase.product != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "product=" + product +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
