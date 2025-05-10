package com.cooking.AcceptanceTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.cooking.core.InventoryManager;
import com.cooking.core.Ingredient;
import com.cooking.core.RestockRequest;

public class InventoryManagementSteps {
    private InventoryManager inventoryManager;
    private List<RestockRequest> restockRequests;
    private List<Ingredient> prioritizedList;
    private static final Logger logger = LoggerFactory.getLogger(InventoryManagementSteps.class);

    @Before
    public void setup(Scenario scenario) {
        this.inventoryManager = new InventoryManager();
        this.restockRequests = new ArrayList<>();
        this.prioritizedList = new ArrayList<>();
        logger.info("Initializing test for scenario: {}", scenario.getName());
    }
    
    @After
    public void dumpState() {
        logger.info("Final state - Restock requests: {}", restockRequests);
        logger.info("Final state - Priority list: {}", prioritizedList);
    }
    
    @Given("the system has ingredients with defined minimum stock levels")
    public void theSystemHasIngredientsWithDefinedMinimumStockLevels() {
        inventoryManager = new InventoryManager();
        inventoryManager.addIngredient(new Ingredient("Saffron", 5, 10, 50));
        logger.debug("Initialized with Saffron: 5/10g (50% threshold)");
    }
    
    @When("the current stock of {string} falls below its minimum level of {int} grams")
    public void verifyStockLevel(String name, int minLevel) {
        Ingredient ingredient = inventoryManager.getIngredient(name);
        logger.debug("Verifying {}: {} < {} = {}", 
            name, ingredient.getCurrentStock(), minLevel,
            ingredient.getCurrentStock() < minLevel);
        assertTrue(ingredient.getCurrentStock() < minLevel);
    }

    @Then("the system should show a restocking alert for {string}")
    public void verifyRestockAlert(String name) {
        assertTrue(inventoryManager.needsRestocking(name));
    }

    @Then("the alert should indicate the current stock level is critical")
    public void verifyCriticalAlert() {
        Ingredient saffron = inventoryManager.getIngredient("Saffron");
        boolean isCritical = saffron.getCurrentStock() <= (saffron.getMinimumLevel() * saffron.getCriticalThreshold() / 100);
        logger.debug("Critical check: {} ≤ {}% of {} = {}", 
            saffron.getCurrentStock(), saffron.getCriticalThreshold(),
            saffron.getMinimumLevel(), isCritical);
        assertTrue(isCritical);
    }

    @Given("the following ingredients are below their minimum levels:")
    public void theFollowingIngredientsAreBelowTheirMinimumLevels(io.cucumber.datatable.DataTable dataTable) {
        inventoryManager = new InventoryManager();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> row : rows) {
            String name = row.get("Name");
            int currentStock = Integer.parseInt(row.get("Current Stock").replace(" grams", "").trim());
            int minLevel = Integer.parseInt(row.get("Minimum Level").replace(" grams", "").trim());
            
            inventoryManager.addIngredient(new Ingredient(name, currentStock, minLevel));
        }
    }

    @When("the system performs its daily stock check")
    public void performDailyStockCheck() {
        this.restockRequests = inventoryManager.generateRestockRequests();
        logger.debug("Generated {} restock requests", restockRequests.size());
    }
    
    @Then("the system should generate restock requests for:")
    public void verifyRestockRequests(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expected = dataTable.asMaps();
        assertEquals("Number of requests mismatch", expected.size(), restockRequests.size());
        for (int i = 0; i < expected.size(); i++) {
            Map<String, String> exp = expected.get(i);
            RestockRequest actual = restockRequests.get(i);
            
            assertEquals(exp.get("Name"), actual.getIngredientName());
            assertEquals(
                Integer.parseInt(exp.get("Quantity Needed").replace(" grams", "")), 
                actual.getQuantityNeeded()
            );
        }
    }

    @Given("the following ingredients need replenishment:")
    public void theFollowingIngredientsNeedReplenishment(io.cucumber.datatable.DataTable dataTable) {
        inventoryManager = new InventoryManager();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> row : rows) {
            String name = row.get("Name");
            int currentStock = Integer.parseInt(row.get("Current Stock").replace(" grams", ""));
            int minLevel = Integer.parseInt(row.get("Minimum Level").replace(" grams", ""));
            int criticalThreshold = Integer.parseInt(row.get("Critical Threshold").replace("%", ""));
            
            inventoryManager.addIngredient(new Ingredient(
                name, 
                currentStock, 
                minLevel, 
                criticalThreshold
            ));
        }
    }

    @When("the system evaluates inventory priorities")
    public void theSystemEvaluatesInventoryPriorities() {
        this.prioritizedList = inventoryManager.getPrioritizedRestockList();
        logger.debug("Priority list contains {} items", prioritizedList.size());
    }

    @Then("{string} should be marked as urgent")
    public void verifyUrgent(String ingredientName) {
        Ingredient ingredient = inventoryManager.getIngredient(ingredientName);
        assertTrue("Ingredient should be urgent when at/below threshold. Current: " + 
                  ingredient.getCurrentStock() + "g, Threshold: " + 
                  (ingredient.getMinimumLevel() * ingredient.getCriticalThreshold() / 100) + "g",
                  ingredient.isUrgent());
    }

    @Then("{string} should appear first in the restocking list")
    public void shouldAppearFirstInTheRestockingList(String ingredientName) {
        assertFalse("Priority list is empty", prioritizedList.isEmpty());
        assertEquals(ingredientName, prioritizedList.get(0).getName());
    }

    @Then("{string} should appear second in the restocking list")
    public void shouldAppearSecondInTheRestockingList(String ingredientName) {
        assertEquals(ingredientName, prioritizedList.get(1).getName());
    }
}