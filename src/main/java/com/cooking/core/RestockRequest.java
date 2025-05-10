package com.cooking.core;

public class RestockRequest {
    private String ingredientName;
    private int quantityNeeded;

    public RestockRequest(String ingredientName, int quantityNeeded) {
        this.ingredientName = ingredientName;
        this.quantityNeeded = quantityNeeded;
    }

    // Getter methods
    public String getIngredientName() {
        return ingredientName;
    }

    public int getQuantityNeeded() {
        return quantityNeeded;
    }
    @Override
    public String toString() {
        return String.format("%s: %d grams needed", ingredientName, quantityNeeded);
    }
}