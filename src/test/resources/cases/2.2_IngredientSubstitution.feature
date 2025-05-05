 Feature: Suggest ingredient substitutions for dietary restrictions
  As a customer
  I want the system to suggest alternatives
  So that I can still enjoy meals safely

  Scenario: Ingredient is unavailable
    Given an ingredient I selected is unavailable
    When I finalize my order
    Then the system should suggest a compatible substitute

  Scenario: Chef approves ingredient substitution
    Given the system suggests a substitution
    When I, as a chef, receive the substitution alert
    Then I should approve or modify the final recipe