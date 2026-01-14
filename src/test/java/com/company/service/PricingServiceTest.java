package com.company.service;

import com.company.model.Customer;
import com.company.model.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Pricing Service Unit Tests")
class PricingServiceTest {

    private final PricingService pricingService = new PricingServiceImpl();

    @ParameterizedTest(name = "Total={0}, Expected Final Amount={1}")
    @CsvSource({
            // GST 18% applied AFTER discount
            "10000, 11210",   // 5% discount → 9500 + 18% GST = 11210
            "25000, 26550"    // 10% discount → 22500 + 18% GST = 26550
    })
    @DisplayName("Should apply discount and GST correctly")
    void shouldApplyDiscountAndGstCorrectly(double total, double expectedFinalAmount) {

        // -------- Arrange --------
        Customer customer = new Customer("C1", true, false);
        List<OrderItem> items = Arrays.asList(
                new OrderItem("P1", total, 1)
        );

        // -------- Act --------
        double finalAmount =
                pricingService.calculateFinalAmount(items, customer, false);

        // -------- Assert --------
        assertEquals(
                expectedFinalAmount,
                finalAmount,
                "Final amount after discount and GST should be correct"
        );
    }
}
