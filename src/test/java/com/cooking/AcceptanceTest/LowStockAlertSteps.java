package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;

public class LowStockAlertSteps {

    @Given("stock levels are below the threshold")
    public void stock_levels_are_below_the_threshold() {
        System.out.println("Ingredient stock below threshold.");
    }

    @When("the system reviews inventory for low stock")
    public void the_system_reviews_inventory_for_low_stock() {
        System.out.println("System reviews inventory for low stock.");
    }

    @Then("I should receive an alert to reorder ingredients")
    public void i_should_receive_an_alert_to_reorder_ingredients() {
        System.out.println("Alert to reorder sent.");
    }
}
