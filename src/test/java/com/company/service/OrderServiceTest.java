package com.company.service;

import com.company.exception.InvalidOrderException;
import com.company.exception.OrderNotFoundException;
import com.company.model.Customer;
import com.company.model.Order;
import com.company.model.OrderItem;
import com.company.model.OrderStatus;
import com.company.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Unit Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PricingService pricingService;

    @Mock
    private ComplianceService complianceService;

    @InjectMocks
    private OrderServiceImpl orderService;

    // ---------- ORDER CREATION TESTS ----------

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
                InvalidOrderException.class,
                () -> orderService.createOrder(order, false),
                "Inactive customer should not be allowed"
        );

        verifyNoInteractions(orderRepository, pricingService, complianceService);
    }

    @Test
    @DisplayName("Should create order successfully when all rules pass")
    void shouldCreateOrderSuccessfully() {

        Customer customer =
                new Customer("C1", true, false);

        Order order = new Order(
                "O2",
                customer,
                Arrays.asList(new OrderItem("P1", 1000, 1)),
                OrderStatus.CREATED,
                1000
        );

        when(pricingService.calculateFinalAmount(any(), any(), anyBoolean()))
                .thenReturn(1180.0);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        orderService.createOrder(order, false);

        verify(complianceService).validateOrder(order);
        verify(pricingService).calculateFinalAmount(any(), any(), anyBoolean());
        verify(orderRepository).save(order);
    }

    // ---------- ORDER CANCELLATION TESTS (STEP 11 – RED) ----------

    @Test
    @DisplayName("Should throw exception when order does not exist")
    void shouldFailWhenOrderDoesNotExist() {

        when(orderRepository.findById("O100"))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.cancelOrder("O100", "Customer request"),
                "Cancelling non-existing order should fail"
        );
    }

    @Test
    @DisplayName("Should throw exception when cancellation reason is missing")
    void shouldFailWhenCancellationReasonIsMissing() {

        Customer customer =
                new Customer("C1", true, false);

        Order order = new Order(
                "O200",
                customer,
                Arrays.asList(new OrderItem("P1", 1000, 1)),
                OrderStatus.CREATED,
                1000
        );

        when(orderRepository.findById("O200"))
                .thenReturn(Optional.of(order));

        assertThrows(
                InvalidOrderException.class,
                () -> orderService.cancelOrder("O200", ""),
                "Cancellation reason is mandatory"
        );
    }

    @Test
    @DisplayName("Should throw exception when order is already cancelled")
    void shouldFailWhenOrderAlreadyCancelled() {

        Customer customer =
                new Customer("C1", true, false);

        Order order = new Order(
                "O300",
                customer,
                Arrays.asList(new OrderItem("P1", 1000, 1)),
                OrderStatus.CANCELLED,
                1000
        );

        when(orderRepository.findById("O300"))
                .thenReturn(Optional.of(order));

        assertThrows(
                InvalidOrderException.class,
                () -> orderService.cancelOrder("O300", "Duplicate cancel"),
                "Already cancelled order cannot be cancelled again"
        );
    }

    @Test
    @DisplayName("Should cancel order successfully when all rules pass")
    void shouldCancelOrderSuccessfully() {

        Customer customer =
                new Customer("C1", true, false);

        Order order = new Order(
                "O400",
                customer,
                Arrays.asList(new OrderItem("P1", 1000, 1)),
                OrderStatus.CREATED,
                1000
        );

        when(orderRepository.findById("O400"))
                .thenReturn(Optional.of(order));

        orderService.cancelOrder("O400", "Customer changed mind");

        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should return order when order id exists")
    void shouldReturnOrderWhenOrderIdExists() {

        Customer customer =
                new Customer("C1", true, false);

        Order order = new Order(
                "O500",
                customer,
                java.util.Arrays.asList(new OrderItem("P1", 1000, 1)),
                OrderStatus.CREATED,
                1000
        );

        when(orderRepository.findById("O500"))
                .thenReturn(java.util.Optional.of(order));

        Order result = orderService.getOrderById("O500");

        // ❌ Will fail until implementation is done
    }

    @Test
    @DisplayName("Should throw exception when order id does not exist")
    void shouldFailWhenOrderIdDoesNotExist() {

        when(orderRepository.findById("O600"))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById("O600"),
                "Order not found should throw exception"
        );
    }

    @Test
    @DisplayName("Should return orders when customer id exists")
    void shouldReturnOrdersWhenCustomerIdExists() {

        when(orderRepository.findByCustomerId("C1"))
                .thenReturn(java.util.Arrays.asList());

        orderService.getOrdersByCustomerId("C1");

        verify(orderRepository).findByCustomerId("C1");
    }

    @Test
    @DisplayName("Should throw exception when customer id is null")
    void shouldFailWhenCustomerIdIsNull() {

        assertThrows(
                InvalidOrderException.class,
                () -> orderService.getOrdersByCustomerId(null),
                "Customer id cannot be null"
        );
    }

}
