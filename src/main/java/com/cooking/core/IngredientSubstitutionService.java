package com.cooking.core;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class IngredientSubstitutionService {
    private static final Logger logger = Logger.getLogger(IngredientSubstitutionService.class.getName());
    private static final Map<String, List<String>> substitutionRules = new HashMap<>();
    private static final Map<String, List<String>> dietaryRestrictions = new HashMap<>();
    private static volatile boolean initialized = false;
    
    private final NotificationService notificationService;
    private String lastUnavailableIngredient;
    private String lastSuggestedSubstitution;

    public IngredientSubstitutionService(NotificationService notificationService) {
        this.notificationService = Objects.requireNonNull(notificationService);
        initializeData();
        verifyInitialization();
    }

    private static synchronized void initializeData() {
        if (initialized) return;
        
        try {
            // Substitution Rules (using immutable lists)
            substitutionRules.put("Cheese", List.of("Nutritional yeast"));
            substitutionRules.put("Milk", List.of("Almond milk", "Oat milk"));
            substitutionRules.put("Butter", List.of("Coconut oil", "Margarine"));
            substitutionRules.put("Egg", List.of("Flaxseed meal", "Applesauce", "Chia seeds"));
            
            // Dietary Restrictions
            dietaryRestrictions.put("Dairy", List.of("milk", "cheese", "butter", "yogurt"));
            
            initialized = true;
            logger.info("Substitution service initialized successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize substitution data", e);
            throw new IllegalStateException("Substitution data initialization failed", e);
        }
    }

    private void verifyInitialization() {
        if (!initialized) {
            throw new IllegalStateException("Service not initialized");
        }
        
        String[] requiredIngredients = {"Cheese", "Milk", "Butter", "Egg"};
        for (String ingredient : requiredIngredients) {
            if (!substitutionRules.containsKey(ingredient) || substitutionRules.get(ingredient).isEmpty()) {
                throw new IllegalStateException("No substitutes found for " + ingredient);
            }
        }
    }

    public Map<String, List<String>> getSubstitutionRules() {
        return Collections.unmodifiableMap(substitutionRules);
    }

    public void markUnavailable(String ingredient) {
    	logger.log(Level.INFO, "Marking ingredient as unavailable: {0} (Called by {1})", 
    	        new Object[]{ingredient, getCallerClass()});
        this.lastUnavailableIngredient = ingredient;
    }
    private String getCallerClass() {
    	String fullName = Thread.currentThread().getStackTrace()[3].getClassName();
        return fullName.substring(fullName.lastIndexOf('.') + 1);
    }

    public String getLastUnavailableIngredient() {
        return lastUnavailableIngredient;
    }

    public void suggestSubstitution(String original, String substitute) {
        logger.log(Level.INFO, "Suggesting substitution: {0} → {1}", 
            new Object[]{original, substitute});
        this.lastSuggestedSubstitution = substitute;
    }

    public List<String> findSubstitutes(String ingredient, String allergy) {
        logger.log(Level.INFO, "Finding substitutes for {0} (allergy: {1})",
            new Object[]{ingredient, allergy != null ? allergy : "none"});
        
        List<String> substitutes = new ArrayList<>();
        
        if (!substitutionRules.containsKey(ingredient)) {
            logger.warning("No substitution rules found for: " + ingredient);
            return substitutes;
        }

        for (String sub : substitutionRules.get(ingredient)) {
            if (allergy == null || !isRestricted(sub, allergy)) {
                substitutes.add(sub);
                logger.log(Level.FINE, "Added valid substitute: {0}", sub);
            }
        }

        if (!substitutes.isEmpty()) {
            String message = createNotificationMessage(ingredient, substitutes);
            logger.log(Level.INFO, "Sending notification: {0}", message);
            notificationService.sendNotification("chef@example.com", message);
        }
        
        return substitutes;
    }

    private String createNotificationMessage(String original, List<String> substitutes) {
        return substitutes.size() == 1 ?
            String.format("Suggested substitution: %s → %s", original, substitutes.get(0)) :
            String.format("Multiple options for %s: %s", original, String.join(", ", substitutes));
    }

    public boolean validateCompatibility(String original, String substitute) {
        boolean valid = substitutionRules.getOrDefault(original, Collections.emptyList())
                                      .contains(substitute);
        logger.log(Level.FINE, "Compatibility {0} → {1}: {2}",
            new Object[]{original, substitute, valid});
        return valid;
    }

    private boolean isRestricted(String ingredient, String allergy) {
        if (!dietaryRestrictions.containsKey(allergy)) return false;
        
        return dietaryRestrictions.get(allergy).stream()
            .anyMatch(restricted -> ingredient.equalsIgnoreCase(restricted) || 
                      ingredient.toLowerCase().contains(restricted.toLowerCase()));
    }

    // For testing purposes only
    public static synchronized void resetForTesting() {
        substitutionRules.clear();
        dietaryRestrictions.clear();
        initialized = false;
    }
}