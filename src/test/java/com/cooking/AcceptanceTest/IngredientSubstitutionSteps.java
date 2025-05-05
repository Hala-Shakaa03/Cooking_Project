package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;

public class IngredientSubstitutionSteps {
	@Given("an ingredient I selected is unavailable")
    public void ingredient_unavailable() {
        System.out.println("Selected ingredient is unavailable.");
    }

    @When("I finalize my order")
    public void finalize_order() {
        System.out.println("Customer finalizes order.");
    }

    @Then("the system should suggest a compatible substitute")
    public void system_suggest_substitute() {
        System.out.println("System suggests compatible substitute.");
    }

    @Given("the system suggests a substitution")
    public void system_suggests_substitution() {
        System.out.println("System has suggested a substitution.");
    }

    @When("I, as a chef, receive the substitution alert")
    public void chef_receives_alert() {
        System.out.println("Chef receives substitution alert.");
    }

    @Then("I should approve or modify the final recipe")
    public void approve_or_modify_recipe() {
        System.out.println("Chef approves or modifies recipe.");
    }
}
