package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;

public class CustomMealRequestSteps {

    @Given("I am placing a new order")
    public void i_am_placing_a_new_order() {
        System.out.println("Customer is placing a new order.");
    }

    @When("I select available ingredients")
    public void i_select_available_ingredients() {
        System.out.println("Customer selects ingredients.");
    }

    @Then("the system should validate and accept my custom meal request")
    public void the_system_should_validate_and_accept_my_custom_meal_request() {
        System.out.println("Custom meal request validated and accepted.");
    }

    @Given("I select incompatible ingredients")
    public void i_select_incompatible_ingredients() {
        System.out.println("Customer selected incompatible ingredients.");
    }

    @When("I submit the custom meal request")
    public void i_submit_the_custom_meal_request() {
        System.out.println("Custom meal request submitted.");
    }

    @Then("the system should display an error and suggest corrections")
    public void the_system_should_display_an_error_and_suggest_corrections() {
        System.out.println("System suggests corrections for invalid combinations.");
    }
	
}
