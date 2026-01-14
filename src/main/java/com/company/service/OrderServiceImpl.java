package com.company.service;

import com.company.exception.InvalidOrderException;
import com.company.exception.OrderNotFoundException;
import com.company.model.Order;
import com.company.model.OrderStatus;
import com.company.repository.OrderRepository;

import java.util.List;

public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final PricingService pricingService;
    private final ComplianceService complianceService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            PricingService pricingService,
                            ComplianceService complianceService) {
        this.orderRepository = orderRepository;
        this.pricingService = pricingService;
        this.complianceService = complianceService;
    }

    public Order createOrder(Order order, boolean festivalOfferEnabled) {

        if (order == null || order.getCustomer() == null || !order.getCustomer().isActive()) {
            throw new InvalidOrderException("Inactive customer is not allowed");
        }

        complianceService.validateOrder(order);

        pricingService.calculateFinalAmount(
                order.getItems(),
                order.getCustomer(),
                festivalOfferEnabled
        );

        order.setStatus(OrderStatus.CREATED);
        return orderRepository.save(order);
    }

    public void cancelOrder(String orderId, String reason) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found: " + orderId));

        if (reason == null || reason.trim().isEmpty()) {
            throw new InvalidOrderException("Cancellation reason is mandatory");
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderException("Order cannot be cancelled in current state");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    // ✅ STEP 14 IMPLEMENTATION

    public Order getOrderById(String orderId) {

        if (orderId == null || orderId.trim().isEmpty()) {
            throw new InvalidOrderException("Order id cannot be null or empty");
        }

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found: " + orderId));
    }

    public List<Order> getOrdersByCustomerId(String customerId) {

        if (customerId == null || customerId.trim().isEmpty()) {
            throw new InvalidOrderException("Customer id cannot be null or empty");
        }

        return orderRepository.findByCustomerId(customerId);
    }
}
