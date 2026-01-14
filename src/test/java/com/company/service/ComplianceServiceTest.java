package com.company.service;

import com.company.exception.ComplianceViolationException;
import com.company.model.Customer;
import com.company.model.Order;
import com.company.model.OrderItem;
import com.company.model.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Compliance Service Unit Tests")
class ComplianceServiceTest {

    private final ComplianceService complianceService = new ComplianceServiceImpl();


    @Test
    @DisplayName("Should throw exception when customer is inactive")
    void shouldFailWhenCustomerIsInactive() {

        Customer inactiveCustomer =
                new Customer("C1", false, false);

        Order order = new Order(
                "O1",
                inactiveCustomer,
                Arrays.asList(new OrderItem("P1", 1000, 1)),
                OrderStatus.CREATED,
                1000
        );

        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validateOrder(order),
                "Inactive customer should not be allowed"
        );
    }

    @Test
    @DisplayName("Should throw exception when order has no items")
    void shouldFailWhenOrderHasNoItems() {

        Customer customer =
                new Customer("C1", true, false);

        Order order = new Order(
                "O2",
                customer,
                Collections.emptyList(),
                OrderStatus.CREATED,
                0
        );

        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validateOrder(order),
                "Order with no items should be rejected"
        );
    }

    @Test
    @DisplayName("Should throw exception when total amount exceeds limit")
    void shouldFailWhenTotalAmountExceedsLimit() {

        Customer customer =
                new Customer("C1", true, false);

        Order order = new Order(
                "O3",
                customer,
                Arrays.asList(new OrderItem("P1", 600000, 1)),
                OrderStatus.CREATED,
                600000
        );

        assertThrows(
                ComplianceViolationException.class,
                () -> complianceService.validateOrder(order),
                "Order exceeding max limit should be rejected"
        );
    }
}
