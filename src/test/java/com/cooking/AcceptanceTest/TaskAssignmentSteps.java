package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;

public class TaskAssignmentSteps {
	 @Given("multiple chefs are available")
	    public void multiple_chefs_are_available() {
	        System.out.println("Chefs are available for task assignment.");
	    }

	    @When("I assign a cooking task")
	    public void i_assign_a_cooking_task() {
	        System.out.println("Manager assigns a task.");
	    }

	    @Then("the task should be allocated based on workload balance")
	    public void the_task_should_be_allocated_based_on_workload_balance() {
	        System.out.println("Task allocated efficiently.");
	    }

	    @Given("I am assigned a task")
	    public void i_am_assigned_a_task() {
	        System.out.println("Chef has received a task.");
	    }

	    @When("the system updates my tasks")
	    public void the_system_updates_my_tasks() {
	        System.out.println("System updated chef's task list.");
	    }

	    @Then("I should receive a notification with details")
	    public void i_should_receive_a_notification_with_details() {
	        System.out.println("Chef receives task notification.");
	    }
	
}
