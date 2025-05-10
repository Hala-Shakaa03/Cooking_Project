package com.cooking.model;

public enum DietaryRestriction {
    VEGAN,
    VEGETARIAN,
    GLUTEN_FREE,
    PEANUT_ALLERGY,
    DAIRY_ALLERGY;
    
    public boolean isViolatedBy(String ingredient) {
        // Implementation would check against known restricted ingredients
        return false;
    }
}