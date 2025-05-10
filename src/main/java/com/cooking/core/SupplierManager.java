package com.cooking.core;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupplierManager {
    private static final Logger logger = LoggerFactory.getLogger(SupplierManager.class);
    
    private final Map<String, String> suppliers = new HashMap<>();
    private final Map<String, List<Map<String, String>>> priceCache = new HashMap<>();
    private final Map<String, Instant> cacheTimestamps = new HashMap<>();

    public List<String> getRegisteredSuppliers() {
        return new ArrayList<>(suppliers.keySet());
    }

    public void registerSupplier(String name, String endpoint) {
    	 if (!suppliers.containsKey(name)) {
    	        suppliers.put(name, endpoint);
    	        logger.debug("Registered supplier: {} ({})", name, endpoint);
    	    }
    }

    public List<Map<String, String>> fetchPrices(String ingredient) {
    	if (isPriceCached(ingredient)) {
            logger.trace("Using cached prices for {}", ingredient);  // Changed to trace
            return priceCache.get(ingredient);
        }
        
        logger.info("Fetching live prices for {}", ingredient);
        List<Map<String, String>> prices = suppliers.entrySet().stream()
            .map(entry -> {
                Map<String, String> priceInfo = new HashMap<>();
                priceInfo.put("supplier", entry.getKey());
                priceInfo.put("price", String.format("%.2f", 8.0 + Math.random() * 5));
                return priceInfo;
            })
            .collect(Collectors.toList());
        
        cachePrices(ingredient, prices);
        return prices;
    }

    private List<Map<String, String>> createMockPrices() {
        return suppliers.entrySet().stream()
            .map(entry -> {
                Map<String, String> priceInfo = new HashMap<>();
                priceInfo.put("supplier", entry.getKey());
                priceInfo.put("price", String.format("%.2f", 8.0 + Math.random() * 5));
                priceInfo.put("available", "1000");
                return priceInfo;
            })
            .collect(Collectors.toList());
    }
  
    public boolean isPriceCached(String ingredient) {
        if (!priceCache.containsKey(ingredient) || !cacheTimestamps.containsKey(ingredient)) {
            return false;
        }
        Instant lastUpdated = cacheTimestamps.get(ingredient);
        return Duration.between(lastUpdated, Instant.now()).toHours() < 1;
    }

    private void cachePrices(String ingredient, List<Map<String, String>> prices) {
    	Instant now = Instant.now();
        priceCache.put(ingredient, prices);
        cacheTimestamps.put(ingredient, now);
        logger.debug("Cached prices for {} (valid until {})", 
            ingredient,
            now.plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.MINUTES));
    }

    public void mockPriceForTesting(String ingredient, String supplier, double price) {
        Map<String, String> mockPrice = new HashMap<>();
        mockPrice.put("supplier", supplier);
        mockPrice.put("price", String.format("%.2f", price));
        
        cachePrices(ingredient, Collections.singletonList(mockPrice));
        logger.info("Set test price: {} = ${} via {}", 
            ingredient, 
            String.format("%.2f", price), 
            supplier);
    }

    public PurchaseOrder generateAutoPurchaseOrder(String ingredient, InventoryManager inventory) {
        Ingredient ing = inventory.getIngredient(ingredient);
        if (ing == null) {
            throw new IllegalArgumentException("Unknown ingredient: " + ingredient);
        }
        
        int needed = ing.getMinimumLevel() * 2;
        List<Map<String, String>> prices = fetchPrices(ingredient);
        
        Map<String, String> bestOffer = prices.stream()
            .min(Comparator.comparingDouble(p -> Double.parseDouble(p.get("price"))))
            .orElseThrow(() -> new IllegalStateException("No prices available"));
        
        return new PurchaseOrder(
            ingredient,
            needed,
            bestOffer.get("supplier"),
            Double.parseDouble(bestOffer.get("price"))
        );
    }

    public void clearCache() {
    	if (!priceCache.isEmpty()) {  // Only log if there was something to clear
            priceCache.clear();
            cacheTimestamps.clear();
            logger.info("Price cache cleared");
        }
    }
}