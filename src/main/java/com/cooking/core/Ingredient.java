package com.cooking.core;


public class Ingredient {
    private String name;
    private int currentStock;
    private int minimumLevel;
    private int criticalThreshold; // percentage of minimum level

    public Ingredient(String name, int currentStock, int minimumLevel) {
        this(name, currentStock, minimumLevel, 30); // default 30% critical threshold
    }

    public Ingredient(String name, int currentStock, int minimumLevel, int criticalThreshold) {
        this.name = name;
        this.currentStock = currentStock;
        this.minimumLevel = minimumLevel;
        this.criticalThreshold = criticalThreshold;
    }

    public boolean needsRestocking() {
        return currentStock < minimumLevel;
    }
    @Override
    public String toString() {
        return String.format("%s (%dg/%dg)", name, currentStock, minimumLevel);
    }

    public boolean isUrgent() {
    	 double thresholdAmount = minimumLevel * (criticalThreshold / 100.0);
    	    return currentStock <= thresholdAmount;
    }
    public int getCriticalThreshold() {
        return criticalThreshold;
    }
    // Getters
    public String getName() { return name; }
    public int getCurrentStock() { return currentStock; }
    public int getMinimumLevel() { return minimumLevel; }
}
