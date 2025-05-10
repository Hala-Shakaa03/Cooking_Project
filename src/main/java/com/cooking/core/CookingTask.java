package com.cooking.core;

public class CookingTask {
public enum Priority { HIGH, MEDIUM, LOW }
    
    private String description;
    private Priority priority;
    private String cuisineType;

    public CookingTask(String description, Priority priority) {
        this.description = description;
        this.priority = priority;
        // Simple heuristic to determine cuisine type from description
        if (description.toLowerCase().contains("lasagna") || 
            description.toLowerCase().contains("tiramisu") || 
            description.toLowerCase().contains("pasta")) {
            cuisineType = "Italian";
        } else if (description.toLowerCase().contains("wellington")) {
            cuisineType = "French";
        } else {
            cuisineType = "General";
        }
    }

    // Getters
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public String getCuisineType() { return cuisineType; }
}