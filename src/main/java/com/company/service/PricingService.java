package com.company.service;

import com.company.model.Customer;
import com.company.model.OrderItem;

import java.util.List;

public interface PricingService {

    double calculateFinalAmount(List<OrderItem> items,
                                Customer customer,
                                boolean festivalOfferEnabled);
}
