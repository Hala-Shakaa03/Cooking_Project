package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;

public class DietaryPreferencesSteps {
	 @Given("I am a registered customer")
	    public void i_am_a_registered_customer() {
	        System.out.println("Registered customer ready.");
	    }

	    @When("I add {string} as my dietary preference and {string} as an allergy")
	    public void add_dietary_preference(String preference, String allergy) {
	        System.out.println("Saved: " + preference + " | " + allergy);
	    }

	    @Then("the system should save these preferences to my profile")
	    public void save_preferences() {
	        System.out.println("Preferences saved to profile.");
	    
}
	    @Given("I am a chef")
	    public void iAmAChef() {
	        System.out.println("Chef ready.");
	    }

	    @When("I open a customers profile")
	    public void iOpenACustomersProfile() {
	        System.out.println("Chef opens customer profile.");
	    }

	    @Then("I should see their dietary preferences and allergies")
	    public void iShouldSeeTheirDietaryPreferencesAndAllergies() {
	        System.out.println("Chef sees customer dietary preferences and allergies.");
	    }

}
