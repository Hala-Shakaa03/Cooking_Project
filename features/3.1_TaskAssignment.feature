 Feature: Assign cooking tasks to chefs
  As a kitchen manager
  I want to assign tasks based on workload and expertise
  So that work is distributed efficiently

   Scenario: Manager assigns task to least busy chef
    Given the following chefs are available:
      | Name   | Email            | Expertise       | Current Tasks |
      | Gordon | gordon@email.com | French Cuisine  | 2             |
      | Jamie  | Jamie@email.com  | Italian Cuisine | 1             |
      | sarah  | Sarah@email.com  | Baking          | 3             |
    When I assign "Prepare vegan lasagna" task with priority "HIGH"
    Then the task should be assigned to "Jamie"
    And "jamie@email.com" should receive a notification

 Scenario: Prevent overloading chef
    Given chef "Gordon" with email "gordon@email.com" has 5 active tasks
    When I try to assign "Prepare beef wellington" task
    Then the system should reject the assignment
    And suggest redistributing workload
    
    Scenario: Assign based on expertise
    Given the following chefs are available:
      | Name   | Email            | Expertise        | Current Tasks |
      | Gordon | gordon@email.com | French Cuisine   | 1             |
      | Jamie  | jamie@email.com  | Italian Cuisine  | 2             |
    When I assign "Prepare tiramisu" task with priority "MEDIUM"
    Then the task should be assigned to "Jamie"