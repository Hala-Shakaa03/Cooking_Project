package com.cooking.model;

public class CustomerProfile {
    private final String id;
    private String dietaryPreference;
    private String allergy;

    public CustomerProfile(String id) {
        this.id = id;
    }

    public void setDietaryPreference(String preference) {
        if (preference == null || preference.trim().isEmpty()) {
            throw new IllegalArgumentException("Dietary preference cannot be empty");
        }
        this.dietaryPreference = preference;
    }

    public void setAllergy(String allergy) {
        this.allergy = "None".equalsIgnoreCase(allergy) ? null : allergy;
    }

    public String getDietaryPreference() {
        return dietaryPreference;
    }

    public String getAllergies() {
        return allergy;
    }

    public String getId() {
        return id;
    }
}