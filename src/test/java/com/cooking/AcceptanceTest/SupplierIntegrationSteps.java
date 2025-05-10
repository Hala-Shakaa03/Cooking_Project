package com.cooking.AcceptanceTest;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.util.*;
import com.cooking.core.SupplierManager;
import com.cooking.core.PurchaseOrder;
import com.cooking.core.InventoryManager;
import com.cooking.core.Ingredient;

public class SupplierIntegrationSteps {
	private SupplierManager supplierManager;
    private List<Map<String, String>> receivedPrices;
    private PurchaseOrder generatedPO;
    private InventoryManager inventoryManager;

    @Before
    public void setup() {
    	 supplierManager = new SupplierManager();
    	    inventoryManager = new InventoryManager();
    	    // Initialize with default suppliers if needed
    	    supplierManager.registerSupplier("FreshFoods", "https://api.freshfoods.com/prices");
    	    supplierManager.registerSupplier("Metro", "https://api.metro.ca/inventory");
    	    supplierManager.clearCache();
    }

    @Given("the following suppliers are available:")
    public void theFollowingSuppliersAreAvailable(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asMaps().forEach(row -> {
            supplierManager.registerSupplier(
                row.get("Supplier"),
                row.get("API Endpoint")
            );
        });
    }

    @When("I request current prices for {string}")
    public void iRequestCurrentPricesFor(String ingredient) {
        receivedPrices = supplierManager.fetchPrices(ingredient);
    }

    @Then("I should receive prices from all suppliers")
    public void iShouldReceivePricesFromAllSuppliers() {
        assertNotNull("Prices should not be null", receivedPrices);
        assertEquals(supplierManager.getRegisteredSuppliers().size(), receivedPrices.size());
    }

    @Then("the system should cache prices for {int} hour")
    public void theSystemShouldCachePricesForHour(Integer hours) {
        assertTrue(supplierManager.isPriceCached("Saffron"));
    }

    @Given("{string} is at critical stock level \\({int} grams)")
    public void isAtCriticalStockLevelGrams(String ingredient, Integer currentStock) {
        // This now just sets current stock, minimum level is set separately
        Ingredient existing = inventoryManager.getIngredient(ingredient);
        if (existing != null) {
            inventoryManager.addIngredient(new Ingredient(
                ingredient,
                currentStock,
                existing.getMinimumLevel(),
                existing.getCriticalThreshold()
            ));
        }
    }


    @Given("the minimum stock level for {string} is {int} grams")
    public void theMinimumStockLevelForIsGrams(String ingredientName, int minLevel) {
        Ingredient ingredient = inventoryManager.getIngredient(ingredientName);
        if (ingredient != null) {
            // Create new ingredient with updated minimum level while preserving current stock
            inventoryManager.addIngredient(new Ingredient(
                ingredientName,
                ingredient.getCurrentStock(),
                minLevel,
                ingredient.getCriticalThreshold()
            ));
        }
       
    }

    @Given("{string} has the following stock levels:")
    public void setIngredientStockLevels(String ingredientName, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        int currentStock = parseGramsValue(data.get("currentStock"));
        int minLevel = parseGramsValue(data.get("minimumLevel"));
        
        inventoryManager.addIngredient(new Ingredient(
            ingredientName,
            currentStock,
            minLevel,
            20
        ));
    }

    private int parseGramsValue(String value) {
        return Integer.parseInt(value.replaceAll("[^0-9]", ""));
    }

    @Given("the best current price is from {string} at ${double} per gram")
    public void theBestCurrentPriceIsFromAt$PerGram(String supplier, Double price) {
        supplierManager.mockPriceForTesting("Saffron", supplier, price);
    }

    @When("the automatic inventory check runs")
    public void theAutomaticInventoryCheckRuns() {
        generatedPO = supplierManager.generateAutoPurchaseOrder("Saffron", inventoryManager);
    }

    @Then("the system should generate a purchase order for {int} grams of {string}")
    public void theSystemShouldGenerateAPurchaseOrderForGramsOf(Integer amount, String ingredient) {
        assertEquals(amount.intValue(), generatedPO.getQuantity());
        assertEquals(ingredient, generatedPO.getIngredientName());
    }

    @Then("the PO should be sent to {string}")
    public void thePOShouldBeSentTo(String expectedSupplier) {
        assertEquals(expectedSupplier, generatedPO.getSupplierName());
    }

    @Then("the PO total should be ${double}")
    public void thePOTotalShouldBe$(Double expectedTotal) {
        assertEquals(expectedTotal, generatedPO.getTotalPrice(), 0.001);
    }
}
