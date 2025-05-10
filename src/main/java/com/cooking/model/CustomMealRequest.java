package com.cooking.model;

import java.util.ArrayList;
import java.util.List;

public class CustomMealRequest {
    private List<String> ingredients;
    private List<DietaryRestriction> dietaryRestrictions;

    public CustomMealRequest() {
        this.ingredients = new ArrayList<>();
        this.dietaryRestrictions = new ArrayList<>();
    }

    public CustomMealRequest(List<String> ingredients, List<DietaryRestriction> dietaryRestrictions) {
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
        this.dietaryRestrictions = dietaryRestrictions != null ? new ArrayList<>(dietaryRestrictions) : new ArrayList<>();
    }

    // Getters and Setters
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
    }

    public List<DietaryRestriction> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(List<DietaryRestriction> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions != null ? new ArrayList<>(dietaryRestrictions) : new ArrayList<>();
    }
}