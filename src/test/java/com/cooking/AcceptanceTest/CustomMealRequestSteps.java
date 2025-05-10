package com.cooking.AcceptanceTest;

import com.cooking.core.*;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.Assert.*;

public class CustomMealRequestSteps {
    private static final Logger logger = Logger.getLogger(CustomMealRequestSteps.class.getName());
    
    private CustomerProfile customerProfile;
    private MealBuilder mealBuilder;
    private MealValidator.ValidationResult validationResult;
    private List<String> suggestedAlternatives;
    private Exception lastException;

    @Before
    public void setup() {
        // Initialize fresh objects for each scenario
        customerProfile = new CustomerProfile("regular");
        mealBuilder = new MealBuilder();
        validationResult = null;
        suggestedAlternatives = null;
        lastException = null;
        logger.info("Test scenario initialized");
    }

    @Given("I am placing a new order")
    public void setupNewOrder() {
        // Already initialized in @Before
        logger.info("New order started");
    }

    @Given("I have specified I am vegan")
    public void setVeganCustomer() {
        customerProfile = new CustomerProfile("vegan");
        logger.info("Customer profile set to vegan");
    }

    @Given("I have a peanut allergy")
    public void setPeanutAllergy() {
        customerProfile = new CustomerProfile("regular");
        customerProfile.addAllergy("peanuts");
        logger.info("Customer profile set with peanut allergy");
    }

    @When("I select available ingredients")
    public void selectValidIngredients() {
        try {
            mealBuilder.withIngredients("tomato", "lettuce", "olive oil");
            validationResult = MealValidator.validate(customerProfile, mealBuilder.build());
            logger.info("Selected compatible ingredients");
        } catch (Exception e) {
            lastException = e;
            logger.warning("Unexpected error: " + e.getMessage());
        }
    }

    @When("I select incompatible ingredients")
    public void selectIncompatibleIngredients() {
        try {
            mealBuilder.withIngredients("dairy", "meat");
            validationResult = MealValidator.validate(customerProfile, mealBuilder.build());
        } catch (IllegalArgumentException e) {
            lastException = e;
            suggestedAlternatives = MealValidator.getSuggestedAlternatives();
            logger.warning("Incompatible ingredients selected: " + e.getMessage());
        }
    }

    @When("I select non-vegan ingredients")
    public void selectNonVeganIngredients() {
    	try {
            mealBuilder.withIngredients("cheese", "chicken");
            validationResult = MealValidator.validate(customerProfile, mealBuilder.build());
        } catch (IllegalArgumentException e) {
            lastException = e;
            List<String> nonVeganItems = List.of("cheese", "chicken");
            suggestedAlternatives = MealValidator.getVeganAlternatives(nonVeganItems);
            logger.info("Non-vegan ingredients detected. Suggested alternatives: " + suggestedAlternatives);
        }
    }

    @When("I select a meal containing peanuts")
    public void selectPeanutIngredients() {
        try {
            mealBuilder.withIngredients("peanut sauce", "chicken");
            validationResult = MealValidator.validate(customerProfile, mealBuilder.build());
        } catch (IllegalArgumentException e) {
            lastException = e;
            suggestedAlternatives = MealValidator.getAllergyAlternatives();
            logger.warning("Allergen ingredients selected: " + e.getMessage());
        }
    }

    @Then("the system should accept my request")
    public void verifyAcceptance() {
        assertNull("Unexpected error: " + (lastException != null ? lastException.getMessage() : ""), 
                 lastException);
        assertTrue("Meal should be accepted", validationResult.isValid());
        logger.info("Meal request successfully accepted");
    }

    @Then("the system should reject my request")
    public void verifyRejection() {
        assertNotNull("Expected validation error", lastException);
        logger.info("Meal request rejected as expected: " + lastException.getMessage());
    }

    @Then("suggest valid combinations")
    public void verifyComboSuggestions() {
        assertNotNull("Should suggest alternatives", suggestedAlternatives);
        assertFalse("Alternatives list empty", suggestedAlternatives.isEmpty());
        logger.info("Suggested combinations: " + suggestedAlternatives);
    }

    @Then("the system should reject my selection")
    public void verifyVeganRejection() {
        assertNotNull("Expected vegan validation error", lastException);
        logger.info("Non-vegan selection rejected: " + lastException.getMessage());
    }

    @Then("suggest vegan alternatives")
    public void verifyVeganSuggestions() {
    	 assertNotNull("System should provide vegan alternatives", suggestedAlternatives);
    	    assertFalse("Alternatives list should not be empty", suggestedAlternatives.isEmpty());
    	    
    	    // Verify we have at least one vegan suggestion
    	    boolean hasVeganSuggestion = suggestedAlternatives.stream()
    	        .anyMatch(alt -> alt.toLowerCase().contains("vegan") || 
    	                        alt.equalsIgnoreCase("tofu") ||
    	                        alt.equalsIgnoreCase("tempeh"));
    	    
    	    assertTrue("Should suggest at least one vegan alternative (found: " + suggestedAlternatives + ")", 
    	              hasVeganSuggestion);
    	    
    }

    @Then("the system should suggest an alternative ingredient")
    public void verifyAllergyAlternative() {
    	 assertNotNull("System should provide allergen-free alternatives", suggestedAlternatives);
    	    logger.info("System suggested: " + suggestedAlternatives);
    	    // Verify no suggestions contain allergens (case insensitive)
    	    assertTrue("All suggestions should be allergen-free",
    	              suggestedAlternatives.stream()
    	                  .noneMatch(a -> a.toLowerCase().contains("peanut")));
    }
}