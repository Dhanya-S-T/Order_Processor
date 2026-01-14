package com.company.model;

public class Customer {

    private String customerId;
    private boolean active;
    private boolean premium;

    public Customer(String customerId, boolean active, boolean premium) {
        this.customerId = customerId;
        this.active = active;
        this.premium = premium;
    }

    public String getCustomerId() {
        return customerId;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPremium() {
        return premium;
    }
}
