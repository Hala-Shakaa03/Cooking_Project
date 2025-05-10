package com.cooking.core;

public class PurchaseOrder {
    private String ingredientName;
    private int quantity;
    private String supplierName;
    private double pricePerUnit;

    public PurchaseOrder(String ingredientName, int quantity, String supplierName, double pricePerUnit) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.pricePerUnit = pricePerUnit;
    }

    // Getters
    public String getIngredientName() { return ingredientName; }
    public int getQuantity() { return quantity; }
    public String getSupplierName() { return supplierName; }
    public double getPricePerUnit() { return pricePerUnit; }
    public double getTotalPrice() { return quantity * pricePerUnit; }
}