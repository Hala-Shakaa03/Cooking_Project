Feature: Assign cooking tasks to chefs
  As a kitchen manager
  I want to assign tasks based on workload and expertise
  So that work is distributed efficiently

  Scenario: Manager assigns a task
    Given multiple chefs are available
    When I assign a cooking task
    Then the task should be allocated based on workload balance

  Scenario: Chef receives task notification
    Given I am assigned a task
    When the system updates my tasks
    Then I should receive a notification with details