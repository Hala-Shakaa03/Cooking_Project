package com.cooking.model;

import java.time.LocalDate;
import java.util.Objects;

public class Order {
    private final String orderId;
    private final LocalDate orderDate;
    private final String mealName;
    private final double price;

    public Order(String orderId, LocalDate orderDate, String mealName, double price) {
        this.orderId = Objects.requireNonNull(orderId, "Order ID cannot be null");
        this.orderDate = Objects.requireNonNull(orderDate, "Order date cannot be null");
        this.mealName = Objects.requireNonNull(mealName, "Meal name cannot be null");
        if (price <= 0) throw new IllegalArgumentException("Price must be positive");
        this.price = price;
    }

    // Getters remain the same
    public String getOrderId() { return orderId; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getMealName() { return mealName; }
    public double getPrice() { return price; }
}