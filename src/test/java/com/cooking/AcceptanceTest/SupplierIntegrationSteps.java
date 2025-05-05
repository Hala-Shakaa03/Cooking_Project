package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;

public class SupplierIntegrationSteps {

    @Given("I need to purchase ingredients")
    public void i_need_to_purchase_ingredients() {
        System.out.println("Kitchen manager needs ingredients.");
    }

    @When("I open supplier options")
    public void i_open_supplier_options() {
        System.out.println("Opening supplier options.");
    }

    @Then("I should see the latest prices")
    public void i_should_see_the_latest_prices() {
        System.out.println("Latest supplier prices displayed.");
    }

    @Given("stock level is critically low")
    public void stock_level_is_critically_low() {
        System.out.println("Critical stock alert.");
    }

    @When("the system checks supplier inventory")
    public void the_system_checks_supplier_inventory() {
        System.out.println("Supplier inventory being checked.");
    }

    @Then("it should automatically create a purchase order")
    public void it_should_automatically_create_a_purchase_order() {
        System.out.println("Purchase order created automatically.");
    }
}
