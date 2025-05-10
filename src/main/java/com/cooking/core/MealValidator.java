package com.cooking.core;

import java.util.*;

public class MealValidator {
	   private static final Map<String, List<String>> VEGAN_ALTERNATIVES = Map.of(
		        "cheese", List.of("vegan cheese", "nutritional yeast"),
		        "milk", List.of("almond milk", "soy milk"),
		        "eggs", List.of("tofu scramble", "apple sauce"),
		        "meat", List.of("tofu", "tempeh", "seitan")
		    );

		    public static List<String> getVeganAlternatives(List<String> nonVeganIngredients) {
		        List<String> alternatives = new ArrayList<>();
		        for (String ingredient : nonVeganIngredients) {
		            if (VEGAN_ALTERNATIVES.containsKey(ingredient)) {
		                alternatives.addAll(VEGAN_ALTERNATIVES.get(ingredient));
		            }
		        }
		        return alternatives.isEmpty() ? 
		            List.of("Ask our chef for vegan options") : alternatives;
		    }
    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() { return valid; }
    }

    public static ValidationResult validate(CustomerProfile profile, List<String> ingredients) {  
    	if (profile.isVegan()) {   	
        List<String> nonVeganItems = ingredients.stream()
                .filter(i -> VEGAN_ALTERNATIVES.keySet().contains(i))
                .toList();
            
            if (!nonVeganItems.isEmpty()) {
                throw new IllegalArgumentException("Contains non-vegan ingredients: " + nonVeganItems);
            }
        }
        
        if (hasAllergens(profile, ingredients)) {
            throw new IllegalArgumentException("Contains allergens");
        }
        
        if (hasIncompatiblePairs(ingredients)) {
            throw new IllegalArgumentException("Incompatible ingredients");
        }
        
        return new ValidationResult(true, "Valid meal");
    }

    public static List<String> getSuggestedAlternatives() {
        return List.of("vegetable oil instead of dairy", "tofu instead of meat");
    }

    public static List<String> getVeganAlternatives() {
    	 return List.of("sunflower seed butter", "soy sauce", "almond butter", "tahini");
    }

    public static List<String> getAllergyAlternatives() {
    	 return List.of("sunflower seed butter", "soy sauce", "almond butter", "tahini");
    }

    private static boolean containsNonVegan(List<String> ingredients) {

        Set<String> nonVegan = Set.of("cheese", "milk", "eggs", "chicken", "beef");
        return ingredients.stream()
            .anyMatch(i -> nonVegan.contains(i.toLowerCase()));
    
    }

    private static boolean hasAllergens(CustomerProfile profile, List<String> ingredients) {
    	return ingredients.stream()
                .anyMatch(i -> i.equalsIgnoreCase("peanuts") || 
                             i.toLowerCase().contains("peanut"));
    }

    private static boolean hasIncompatiblePairs(List<String> ingredients) {
        return ingredients.contains("dairy") && ingredients.contains("meat");
    }



}