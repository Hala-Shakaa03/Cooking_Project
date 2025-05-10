package com.cooking.AcceptanceTest;

import com.cooking.core.OrderHistoryManager;
import com.cooking.core.UserRole;
import com.cooking.model.Order;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class OrderHistorySteps {
    private static final Logger logger = Logger.getLogger(OrderHistorySteps.class.getName());
    
    private OrderHistoryManager orderManager;
    private String currentCustomerId;
    private String currentRequesterId;
    private List<Order> retrievedOrders;
    private Exception lastException;

    @Before
    public void setup() {
        orderManager = new OrderHistoryManager();
        lastException = null;
        retrievedOrders = null;
    }

    @Given("customer {string} has {int} past orders")
    public void customerHasPastOrders(String customerId, Integer orderCount) {
        currentCustomerId = customerId;
        for (int i = 1; i <= orderCount; i++) {
            Order order = new Order(
                "ORD-" + UUID.randomUUID().toString().substring(0, 8),
                LocalDate.now().minusDays(i),
                "Meal-" + i,
                15.99 + i
            );
            orderManager.addOrder(customerId, order);
        }
    }

    private void viewOrderHistory(UserRole role, String requesterId) {
        currentRequesterId = requesterId;
        String roleName = role.name().toLowerCase();
        
        logger.info(String.format(
            "%s %s viewing order history for customer %s",
            roleName, currentRequesterId, currentCustomerId
        ));
        
        try {
            retrievedOrders = orderManager.getOrderHistory(
                currentCustomerId, 
                role,
                currentRequesterId
            );
            
            if (role == UserRole.UNAUTHORIZED) {
                logger.warning("SECURITY: Unauthorized access was granted!");
            } else {
                logger.info(String.format(
                    "%s access successful - found %d orders",
                    roleName, retrievedOrders.size()
                ));
            }
        } catch (SecurityException e) {
            lastException = e;
            logger.info(String.format(
                "Expected %s access denied: %s",
                roleName, e.getMessage()
            ));
        } catch (Exception e) {
            lastException = e;
            logger.severe(String.format(
                "Unexpected error during %s access: %s",
                roleName, e.getMessage()
            ));
        }
    }

    @When("customer views the order history")
    public void customerViewsTheOrderHistory() {
        viewOrderHistory(UserRole.CUSTOMER, currentCustomerId);
    }

    @When("chef views the order history")
    public void chefViewsTheOrderHistory() {
        viewOrderHistory(UserRole.CHEF, "chef-1");
    }

    @When("admin views the order history")
    public void adminViewsTheOrderHistory() {
        viewOrderHistory(UserRole.ADMIN, "admin-1");
    }

    @When("unauthorized user views the order history")
    public void unauthorizedUserViewsTheOrderHistory() {
        viewOrderHistory(UserRole.UNAUTHORIZED, "unauth-1");
    }

    @Then("they should see {int} orders")
    public void theyShouldSeeOrders(Integer expectedCount) {
        if (lastException != null) {
            assertEquals(0, expectedCount.intValue());
        } else {
            assertEquals(expectedCount.intValue(), retrievedOrders.size());
        }
    }

    @Then("the response status should be success")
    public void theResponseStatusShouldBeSuccess() {
        assertNull("Expected success but got exception: " + 
                  (lastException != null ? lastException.getMessage() : ""), 
                  lastException);
    }

    @Then("the response status should be denied")
    public void theResponseStatusShouldBeDenied() {
        assertNotNull("Expected access denied exception", lastException);
        assertEquals("Access denied", lastException.getMessage());
    }
}