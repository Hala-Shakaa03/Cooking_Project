package com.cooking.AcceptanceTest;

import com.cooking.core.CustomerProfileManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;
import java.util.logging.Logger;

public class DietaryPreferencesSteps {
    private static final Logger logger = Logger.getLogger(DietaryPreferencesSteps.class.getName());
    
    private CustomerProfileManager profileManager;
    private String currentCustomerId;
    private String currentRole; // "customer", "chef", or "unauthorized"
    private Exception lastException;
    private CustomerProfileManager.CustomerProfile viewedProfile;
    
    @Before
    public void setup(Scenario scenario) {
        profileManager = new CustomerProfileManager();
        currentCustomerId = "cust-" + scenario.getName().hashCode();
        currentRole = "customer";
        profileManager.setDatabaseAvailable(true);
        logger.info("Starting scenario: " + scenario.getName());
    }
    
    @After
    public void tearDown() {
        logger.info("Cleaning up test data");
        profileManager = null;
        lastException = null;
        viewedProfile = null;
    }

    private void executeSafely(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            lastException = e;
            logger.warning("Operation failed: " + e.getMessage());
        }
    }

    // Scenario Outline: Customer saves valid dietary preferences and allergies
    @Given("I am a registered customer")
    public void registerCustomer() {
        currentRole = "customer";
        logger.info("Registered customer with ID: " + currentCustomerId);
    }

    @When("I set dietary preferences to {string} and allergy to {string}")
    public void savePreferences(String preferences, String allergies) {
        logger.info("Saving - Preferences: " + preferences + ", Allergies: " + allergies);
        executeSafely(() -> profileManager.savePreferences(currentCustomerId, preferences, allergies));
    }

    @Then("the system should save them exactly")
    public void verifySaveOperation() {
        assertNull("Save failed: " + (lastException != null ? lastException.getMessage() : ""), 
                 lastException);
        assertTrue("Profile not found", profileManager.containsProfile(currentCustomerId));
    }

    @Then("the saved data should match {string} and {string}")
    public void verifySavedContent(String expectedPrefs, String expectedAllergies) {
        CustomerProfileManager.CustomerProfile profile = 
            profileManager.getProfile(currentCustomerId, "chef");
        assertNotNull("Profile is null", profile);
        assertEquals("Preferences mismatch", expectedPrefs, profile.getDietaryPreferences());
        assertEquals("Allergies mismatch", expectedAllergies, profile.getAllergies());
    }

    // Scenario: Authorized chef views customer profile
    @Given("a customer has saved preferences {string} and allergy {string}")
    public void setupTestData(String preferences, String allergies) {
        profileManager.savePreferences(currentCustomerId, preferences, allergies);
        logger.info("Test data created: " + preferences + "/" + allergies);
    }

    @Given("I am a chef")
    public void authenticateAsChef() {
        currentRole = "chef";
        logger.info("Authenticated as chef");
    }

    @When("I view the customer's profile")
    public void viewCustomerProfile() {
        executeSafely(() -> 
            viewedProfile = profileManager.getProfile(currentCustomerId, currentRole));
    }

    @Then("I should see their dietary preferences and allergies")
    public void verifyProfileVisible() {
        assertNull("View failed", lastException);
        assertNotNull("Profile not visible", viewedProfile);
    }

    @Then("the displayed preferences should be {string}")
    public void verifyDisplayedPreferences(String expected) {
        assertEquals(expected, viewedProfile.getDietaryPreferences());
    }

    @Then("the displayed allergies should be {string}")
    public void verifyDisplayedAllergies(String expected) {
        assertEquals(expected, viewedProfile.getAllergies());
    }

    // Scenario: Unauthorized profile access
    @Given("a customer exists in the system")
    public void customerExists() {
        profileManager.savePreferences(currentCustomerId, "Test", "None");
        logger.info("Test customer created");
    }

    @Given("I am not authorized")
    public void setUnauthorized() {
        currentRole = "unauthorized";
        logger.info("Set as unauthorized user");
    }

    @When("I try to view a customer's preferences")
    public void attemptUnauthorizedAccess() {
        executeSafely(() -> 
            viewedProfile = profileManager.getProfile(currentCustomerId, currentRole));
    }

    // Scenario: Profile save during system outage
    @Given("the database is unavailable")
    public void disableDatabase() {
        profileManager.setDatabaseAvailable(false);
        logger.warning("Database disabled for testing");
    }

    @When("I try to save new preferences {string} and allergy {string}")
    public void attemptSaveDuringOutage(String preferences, String allergies) {
        executeSafely(() -> 
            profileManager.savePreferences(currentCustomerId, preferences, allergies));
    }

    @Then("the system should not have the new preferences")
    public void verifyNonPersistence() {
        profileManager.setDatabaseAvailable(true); // Restore to verify
        assertFalse("Profile was persisted during outage",
                  profileManager.containsProfile(currentCustomerId));
    }

    // Common error verification
    @Then("I should receive {string}")
    public void verifyErrorMessage(String expectedMessage) {
        assertNotNull("Expected exception was not thrown", lastException);
        assertEquals("Error message mismatch", 
                   expectedMessage, 
                   lastException.getMessage());
        logger.info("Verified error message: " + expectedMessage);
    }
}