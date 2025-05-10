package com.cooking.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager {
	 private static final Logger logger = LoggerFactory.getLogger(InventoryManager.class);
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public Ingredient getIngredient(String name) {
        return ingredients.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean needsRestocking(String ingredientName) {
        Ingredient ingredient = getIngredient(ingredientName);
        return ingredient != null && ingredient.needsRestocking();
    }
    public void validateRestockRequest(RestockRequest request) {
        Ingredient ingredient = getIngredient(request.getIngredientName());
        if (ingredient == null) {
            throw new IllegalStateException("Unknown ingredient: " + request.getIngredientName());
        }
        if (request.getQuantityNeeded() <= 0) {
            throw new IllegalStateException("Invalid quantity for " + request.getIngredientName());
        }
    }
    public List<RestockRequest> generateRestockRequests() {
        logger.info("Generating restock requests...");
        List<RestockRequest> requests = ingredients.stream()
            .filter(ingredient -> {
                boolean needsRestock = ingredient.needsRestocking();
                logger.debug("Checking {}: current={}, min={}, needsRestock={}", 
                    ingredient.getName(), 
                    ingredient.getCurrentStock(), 
                    ingredient.getMinimumLevel(),
                    needsRestock);
                return needsRestock;
            })
            .map(ingredient -> {
                int quantityNeeded = ingredient.getMinimumLevel() - ingredient.getCurrentStock();
                logger.info("Creating restock request for {}: {} grams needed", 
                    ingredient.getName(), 
                    quantityNeeded);
                return new RestockRequest(ingredient.getName(), quantityNeeded);
            })
            .collect(Collectors.toList());
        
        logger.info("Generated {} restock requests", requests.size());
        return requests;
    }
   /* public List<RestockRequest> generateRestockRequests() {
    	   return ingredients.stream()
    	            .filter(Ingredient::needsRestocking)
    	            .map(ingredient -> new RestockRequest(
    	                    ingredient.getName(),
    	                    Math.max(ingredient.getMinimumLevel() - ingredient.getCurrentStock(), 0)))
    	            .collect(Collectors.toList());
    }*/

    public List<Ingredient> getPrioritizedRestockList() {
    	return ingredients.stream()
                .filter(Ingredient::needsRestocking)
                .sorted((a, b) -> {
                    // First sort by urgency (critical items first)
                    if (a.isUrgent() != b.isUrgent()) {
                        return Boolean.compare(b.isUrgent(), a.isUrgent());
                    }
                    // Then by how far below minimum they are
                    return Integer.compare(
                        (b.getMinimumLevel() - b.getCurrentStock()),
                        (a.getMinimumLevel() - a.getCurrentStock())
                    );
                })
                .collect(Collectors.toList());
    }
    
    
}
