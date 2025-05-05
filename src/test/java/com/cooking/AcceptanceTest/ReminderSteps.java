package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;

public class ReminderSteps {

    @Given("I am a registered customer with an upcoming order")
    public void iAmARegisteredCustomerWithAnUpcomingOrder() {
        System.out.println("Registered customer has an upcoming order.");
    }

    @When("the scheduled delivery time is approaching")
    public void theScheduledDeliveryTimeIsApproaching() {
        System.out.println("Delivery time is approaching.");
    }

    @Then("I should receive a delivery notification")
    public void iShouldReceiveADeliveryNotification() {
        System.out.println("Customer received delivery notification.");
    }

    @Given("the chef has scheduled cooking tasks")
    public void theChefHasScheduledCookingTasks() {
        System.out.println("Chef has scheduled cooking tasks.");
    }

    @When("the task time is near")
    public void theTaskTimeIsNear() {
        System.out.println("Task time is near.");
    }

    @Then("the system should send a cooking task reminder")
    public void theSystemShouldSendACookingTaskReminder() {
        System.out.println("System sent cooking task reminder.");
    }
}
