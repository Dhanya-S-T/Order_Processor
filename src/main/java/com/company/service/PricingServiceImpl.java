package com.company.service;

import com.company.model.Customer;
import com.company.model.OrderItem;

import java.util.List;

public class PricingServiceImpl implements PricingService {

    private static final double GST_RATE = 0.18;
    private static final double MAX_DISCOUNT_PERCENT = 25.0;

    @Override
    public double calculateFinalAmount(List<OrderItem> items,
                                       Customer customer,
                                       boolean festivalOfferEnabled) {

        double totalAmount = calculateTotal(items);
        double discountPercent = calculateDiscountPercentage(
                totalAmount, customer, festivalOfferEnabled
        );

        double discountedAmount =
                totalAmount - (totalAmount * discountPercent / 100);

        double gstAmount = discountedAmount * GST_RATE;

        return roundToTwoDecimals(discountedAmount + gstAmount);
    }

    // -------- helper methods (private logic allowed) --------

    private double calculateTotal(List<OrderItem> items) {
        double total = 0.0;
        for (OrderItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    private double calculateDiscountPercentage(double totalAmount,
                                               Customer customer,
                                               boolean festivalOfferEnabled) {

        double discount = 0.0;

        if (totalAmount >= 25_000) {
            discount += 10;
        } else if (totalAmount >= 10_000) {
            discount += 5;
        }

        if (customer != null && customer.isPremium()) {
            discount += 5;
        }

        if (festivalOfferEnabled) {
            discount += 5;
        }

        return Math.min(discount, MAX_DISCOUNT_PERCENT);
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
