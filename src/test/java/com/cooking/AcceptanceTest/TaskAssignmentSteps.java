package com.cooking.AcceptanceTest;

import com.cooking.core.*;
import io.cucumber.java.en.*;
import io.cucumber.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import java.util.*;

public class TaskAssignmentSteps {
    private static final Logger logger = LoggerFactory.getLogger(TaskAssignmentSteps.class);
    
    private KitchenManager kitchenManager;
    private Chef assignedChef;
    private boolean assignmentRejected;
    private List<Chef> availableChefs = new ArrayList<>();

    @Before
    public void setUp() {
        logger.info("Initializing test setup...");
        kitchenManager = new KitchenManager();
        availableChefs.clear();
        logger.debug("KitchenManager initialized and available chefs list cleared");
    }

    @Given("the following chefs are available:")
    public void theFollowingChefsAreAvailable(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> chefsData = dataTable.asMaps(String.class, String.class);
        logger.info("Setting up available chefs:");
        
        for (Map<String, String> chefData : chefsData) {
            Chef chef = new Chef(
                chefData.get("Name"),
                chefData.get("Email"),
                chefData.get("Expertise"),
                Integer.parseInt(chefData.get("Current Tasks"))
            );
            availableChefs.add(chef);
            logger.debug("Added chef: {} ({}), Expertise: {}, Current Tasks: {}", 
                chef.getName(), chef.getEmail(), chef.getExpertise(), chef.getCurrentTasks());
        }
        kitchenManager.setAvailableChefs(availableChefs);
        logger.info("Total {} chefs added to kitchen manager", availableChefs.size());
    }

    @When("I assign {string} task with priority {string}")
    public void iAssignTaskWithPriority(String taskDescription, String priority) {
        logger.info("Attempting to assign task: '{}' with priority: {}", taskDescription, priority);
        CookingTask.Priority taskPriority = CookingTask.Priority.valueOf(priority);
        CookingTask task = new CookingTask(taskDescription, taskPriority);
        assignedChef = kitchenManager.assignTask(task);
        
        if (assignedChef != null) {
            logger.info("Task assigned to chef: {} ({} tasks)", 
                assignedChef.getName(), assignedChef.getCurrentTasks());
        }
    }

    @Then("the task should be assigned to {string}")
    public void theTaskShouldBeAssignedTo(String expectedChefName) {
        logger.info("Verifying task was assigned to: {}", expectedChefName);
        assertNotNull("No chef was assigned the task", assignedChef);
        assertEquals(expectedChefName, assignedChef.getName());
        logger.info("Assignment verification successful - task assigned to {}", expectedChefName);
    }

    @Then("{string} should receive a notification")
    public void shouldReceiveANotification(String expectedEmail) {
        logger.info("Verifying notification sent to: {}", expectedEmail);
        assertEquals(expectedEmail.toLowerCase(), assignedChef.getEmail().toLowerCase());
        assertTrue(kitchenManager.wasNotificationSent(assignedChef));
        logger.info("Notification verification successful for {}", expectedEmail);
    }

    @Given("chef {string} with email {string} has {int} active tasks")
    public void chefWithEmailHasActiveTasks(String name, String email, Integer taskCount) {
        logger.info("Setting up chef {} with {} active tasks", name, taskCount);
        Chef chef = new Chef(name, email, "General", taskCount);
        kitchenManager.addChef(chef);
        logger.debug("Added chef: {} ({} tasks)", name, taskCount);
    }

    @When("I try to assign {string} task")
    public void iTryToAssignTask(String taskDescription) {
        logger.info("Attempting to assign task: '{}' to overloaded chef", taskDescription);
        try {
            kitchenManager.assignTask(new CookingTask(taskDescription, CookingTask.Priority.MEDIUM));
            logger.warn("Task assignment unexpectedly succeeded");
        } catch (IllegalStateException e) {
            assignmentRejected = true;
            logger.info("Task assignment rejected as expected: {}", e.getMessage());
        }
    }

    @Then("the system should reject the assignment")
    public void theSystemShouldRejectTheAssignment() {
        logger.info("Verifying task assignment was rejected");
        assertTrue(assignmentRejected);
        logger.info("Assignment rejection verified successfully");
    }

    @Then("suggest redistributing workload")
    public void suggestRedistributingWorkload() {
        logger.info("Verifying workload redistribution suggestion");
        assertTrue(kitchenManager.getWorkloadRedistributionSuggested());
        logger.info("Workload redistribution suggestion verified");
    }
}