package com.epam.store.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private Date date;
    private List<Purchase> purchaseList;

    public Order() {
    }

    public Order(List<Purchase> purchaseList, Date date) {
        this.purchaseList = purchaseList;
        this.date = date;
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public int getItemsAmount() {
        return purchaseList.size();
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (Purchase purchase : purchaseList) {
            totalPrice = totalPrice.add(purchase.getPrice().getValue());
        }
        return totalPrice;
    }

    private enum Status {
        PAID("paid"), UNPAID("Unpaid"), NOT_FULLY_PAID("Not Fully Paid");
        private String name;

        private Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
