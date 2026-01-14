package com.company.service;

import com.company.exception.ComplianceViolationException;
import com.company.model.Order;
import com.company.model.OrderItem;

import java.util.List;

public class ComplianceServiceImpl implements ComplianceService {

    private static final double MAX_ORDER_AMOUNT = 500000.0;

    @Override
    public void validateOrder(Order order) {

        if (order == null) {
            throw new ComplianceViolationException("Order cannot be null");
        }

        if (order.getCustomer() == null || !order.getCustomer().isActive()) {
            throw new ComplianceViolationException("Customer is inactive");
        }

        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) {
            throw new ComplianceViolationException("Order must contain at least one item");
        }

        if (order.getTotalAmount() > MAX_ORDER_AMOUNT) {
            throw new ComplianceViolationException("Order amount exceeds allowed limit");
        }
    }
}
