package com.company.model;

public class OrderItem {

    private String productId;
    private double price;
    private int quantity;

    public OrderItem(String productId, double price, int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
