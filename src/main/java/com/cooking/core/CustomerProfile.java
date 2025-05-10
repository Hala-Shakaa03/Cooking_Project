package com.cooking.core;

import java.util.*;

public class CustomerProfile {
    private final String dietaryPreference;
    private final Set<String> allergies = new HashSet<>();

    public CustomerProfile(String dietaryPreference) {
        this.dietaryPreference = dietaryPreference;
    }

    public void addAllergy(String allergen) {
        allergies.add(allergen);
    }

    public boolean hasAllergy(String ingredient) {
        return allergies.contains(ingredient);
    }

    public boolean isVegan() {
        return "vegan".equals(dietaryPreference);
    }
}