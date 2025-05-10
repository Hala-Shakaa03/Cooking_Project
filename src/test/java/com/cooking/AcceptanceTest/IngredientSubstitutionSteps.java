package com.cooking.AcceptanceTest;

import com.cooking.core.*;
import com.cooking.model.*;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import java.util.*;
import static org.junit.Assert.*;
import java.util.logging.Logger;

public class IngredientSubstitutionSteps {
    private static final Logger logger = Logger.getLogger(IngredientSubstitutionSteps.class.getName());
    
    private IngredientSubstitutionService subService;
    private NotificationService notificationService;
    private Chef assignedChef;
    private String currentAllergy;
    private List<String> suggestedSubstitutes;
    private long scenarioStartTime;
    
    @Before
    public void setup(Scenario scenario) {
        scenarioStartTime = System.currentTimeMillis();
        logger.info(String.format("=== START SCENARIO: %s [%tT.%tL] ===",
            scenario.getName(),
            scenarioStartTime,
            scenarioStartTime));
        
        // Initialize services
        IngredientSubstitutionService.resetForTesting();
        notificationService = new MockNotificationService();
        subService = new IngredientSubstitutionService(notificationService);
        assignedChef = new Chef("chef@example.com", notificationService);
        
        // Reset state
        currentAllergy = null;
        suggestedSubstitutes = null;
        
        logger.info("[SERVICE] INIT: Service ready");
    }

    @After
    public void teardown(Scenario scenario) {
        long duration = System.currentTimeMillis() - scenarioStartTime;
        logger.info(String.format("=== END SCENARIO: %s [%dms] ===",
            scenario.getName(),
            duration));
    }

    @Given("the ingredient {string} is unavailable")
    public void theIngredientIsUnavailable(String ingredient) {
        subService.markUnavailable(ingredient);
    }

    @When("the system checks for substitutes")
    public void theSystemChecksForSubstitutes() {
        String ingredient = subService.getLastUnavailableIngredient();
        logger.info("[TEST] ACTION: Checking substitutes for " + ingredient);
        
        suggestedSubstitutes = subService.findSubstitutes(ingredient, currentAllergy);
        
        logger.info(String.format("[TEST] VERIFY: Found %d substitutes", 
            suggestedSubstitutes.size()));
    }

    @Then("it should suggest {string} as a replacement")
    public void itShouldSuggestAsAReplacement(String expectedSub) {
        assertTrue("[TEST] FAIL: Substitute '" + expectedSub + "' not found",
                 suggestedSubstitutes.contains(expectedSub));
        logger.info("[TEST] VERIFY: Contains expected substitute " + expectedSub);
    }

    @Then("notify the assigned chef")
    public void notifyTheAssignedChef() {
        String unavailable = subService.getLastUnavailableIngredient();
        String substitute = suggestedSubstitutes.get(0);
        
        MockNotificationService mockService = (MockNotificationService) notificationService;
        assertTrue("[TEST] FAIL: Chef not notified about substitution",
                 mockService.wasNotificationSentAbout(unavailable, substitute));
        logger.info("[TEST] VERIFY: Chef notified about " + unavailable + " → " + substitute);
    }

    @Given("the system suggested {string} to replace {string}")
    public void theSystemSuggestedToReplace(String substitute, String original) {
        subService.suggestSubstitution(original, substitute);
        logger.info("[SERVICE] SUGGEST: " + original + " → " + substitute);
    }

    @When("the chef {string} receives the alert")
    public void theChefReceivesTheAlert(String chefEmail) {
        assertEquals(chefEmail, assignedChef.getEmail());
        logger.info("[TEST] VERIFY: Chef " + chefEmail + " received alert");
    }

    @Then("the chef should be able to:")
    public void theChefShouldBeAbleTo(io.cucumber.datatable.DataTable dataTable) {
        dataTable.asMaps().forEach(action -> {
            switch (action.get("Action")) {
                case "Approve":
                    assertTrue(assignedChef.canApproveSubstitution());
                    logger.info("[TEST] VERIFY: Chef can approve substitutions");
                    break;
                case "Modify":
                    assertTrue(assignedChef.canModifySubstitution());
                    logger.info("[TEST] VERIFY: Chef can modify substitutions");
                    break;
                default:
                    fail("Unknown action type");
            }
        });
    }

    @Given("the customer has a {string} allergy")
    public void theCustomerHasAAllergy(String allergy) {
        this.currentAllergy = allergy;
        logger.info("[TEST] SET: Customer allergy - " + allergy);
    }

    @Given("selected {string} in their recipe")
    public void selectedInTheirRecipe(String ingredient) {
        theIngredientIsUnavailable(ingredient);
    }

    @When("the system suggests substitutes")
    public void theSystemSuggestsSubstitutes() {
        theSystemChecksForSubstitutes();
    }

    @When("the system searches for substitutes")
    public void theSystemSearchesForSubstitutes() {
        String savedAllergy = this.currentAllergy;
        try {
            this.currentAllergy = null;
            theSystemChecksForSubstitutes();
        } finally {
            this.currentAllergy = savedAllergy;
        }
    }

    @Then("it should only show dairy-free options like {string}")
    public void itShouldOnlyShowDairyFreeOptionsLike(String expectedSub) {
        assertTrue(suggestedSubstitutes.contains(expectedSub));
        
        List<String> dairyTerms = List.of("milk", "cheese", "butter", "yogurt");
        suggestedSubstitutes.forEach(sub -> 
            dairyTerms.forEach(dairy -> 
                assertFalse(sub.toLowerCase().contains(dairy.toLowerCase()))
            )
        );
        logger.info("[TEST] VERIFY: All substitutes are dairy-free");
    }

    @Then("validate compatibility with the recipe")
    public void validateCompatibilityWithTheRecipe() {
        String original = subService.getLastUnavailableIngredient();
        String substitute = suggestedSubstitutes.get(0);
        
        assertTrue(subService.validateCompatibility(original, substitute));
        logger.info("[TEST] VERIFY: " + original + " → " + substitute + " is VALID");
    }

    @Given("the customer has no dietary restrictions")
    public void theCustomerHasNoDietaryRestrictions() {
        this.currentAllergy = null;
        logger.info("[TEST] SET: No dietary restrictions");
    }

    @Then("it should display all options:")
    public void itShouldDisplayAllOptions(io.cucumber.datatable.DataTable dataTable) {
        List<String> expectedOptions = dataTable.asList();
        assertEquals(expectedOptions, suggestedSubstitutes);
        logger.info("[TEST] VERIFY: All expected options match");
    }
}