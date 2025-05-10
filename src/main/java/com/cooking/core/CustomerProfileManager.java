package com.cooking.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages storage and retrieval of customer dietary profiles
 * with thread-safe operations and database availability simulation.
 */
public class CustomerProfileManager {
    private final Map<String, CustomerProfile> customerProfiles = 
        Collections.synchronizedMap(new HashMap<>());
    private volatile boolean databaseAvailable = true;

    /**
     * Immutable customer profile container
     */
    public static final class CustomerProfile {
        private final String dietaryPreferences;
        private final String allergies;

        public CustomerProfile(String dietaryPreferences, String allergies) {
            this.dietaryPreferences = dietaryPreferences;
            this.allergies = allergies;
        }

        public String getDietaryPreferences() {
            return dietaryPreferences;
        }

        public String getAllergies() {
            return allergies;
        }

        @Override
        public String toString() {
            return String.format("[Preferences: %s, Allergies: %s]", 
                dietaryPreferences, allergies);
        }
    }

    /**
     * Saves customer dietary preferences
     * @param customerId Unique customer identifier
     * @param preferences Dietary preferences (e.g., "Vegan", "Gluten-Free")
     * @param allergies Food allergies (e.g., "Peanuts", "Dairy")
     * @throws IllegalStateException if database is unavailable
     * @throws IllegalArgumentException if customerId is invalid
     */
    public synchronized void savePreferences(String customerId, 
                                           String preferences, 
                                           String allergies) {
        if (!databaseAvailable) {
            throw new IllegalStateException("System unavailable");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (preferences == null || allergies == null) {
            throw new IllegalArgumentException("Preferences and allergies cannot be null");
        }
        
        customerProfiles.put(customerId, 
            new CustomerProfile(preferences.trim(), allergies.trim()));
    }

    /**
     * Retrieves customer profile with authorization check
     * @param customerId Unique customer identifier
     * @param requesterRole Role of the requesting user ("chef" or others)
     * @return CustomerProfile if found and authorized
     * @throws SecurityException if requester is not authorized
     */
    public synchronized CustomerProfile getProfile(String customerId, 
                                                String requesterRole) {
        if (!"chef".equals(requesterRole)) {
            throw new SecurityException("Access denied");
        }
        return customerProfiles.get(customerId);
    }

    /**
     * Gets a snapshot of all stored profiles (thread-safe)
     * @return Unmodifiable copy of current profiles
     */
    public synchronized Map<String, CustomerProfile> getAllProfiles() {
        return new HashMap<>(customerProfiles);
    }

    /**
     * Simulates database availability for testing
     * @param available true to enable database operations
     */
    public synchronized void setDatabaseAvailable(boolean available) {
        this.databaseAvailable = available;
    }

    /**
     * Diagnostic method to check profile existence
     * @param customerId Customer identifier to check
     * @return true if profile exists
     */
    public synchronized boolean containsProfile(String customerId) {
        return customerProfiles.containsKey(customerId);
    }

    /**
     * Clears all profiles (primarily for testing)
     */
    public synchronized void clearAllProfiles() {
        customerProfiles.clear();
    }
}