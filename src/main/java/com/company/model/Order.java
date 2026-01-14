package com.company.model;

import java.util.List;

public class Order {

    private String orderId;
    private Customer customer;
    private List<OrderItem> items;
    private OrderStatus status;
    private double totalAmount;

    public Order(String orderId,
                 Customer customer,
                 List<OrderItem> items,
                 OrderStatus status,
                 double totalAmount) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = items;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
