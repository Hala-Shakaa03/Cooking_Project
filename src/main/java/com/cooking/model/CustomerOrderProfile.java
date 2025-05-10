// CustomerOrderProfile.java
package com.cooking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerOrderProfile {
    private final String customerId;
    private final List<Order> pastOrders;
    
    public CustomerOrderProfile(String customerId) {
        this.customerId = customerId;
        this.pastOrders = new ArrayList<>();
    }
    
    public void addPastOrder(Order order) {
        pastOrders.add(order);
    }
    
    public void clearOrders() {
        pastOrders.clear();
    }
    
    public List<Order> getPastOrders() {
        return Collections.unmodifiableList(pastOrders);
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public boolean loadOrderHistory() {
        // In real implementation, would call a service
        return !pastOrders.isEmpty();
    }
}