Feature: Custom Meal Requests
  As a customer
  I want to select ingredients and customize my meal
  So that I can personalize my order

  Scenario: Create valid custom meal
    Given I am placing a new order
    When I select available ingredients
    Then the system should accept my request

  Scenario: Validate incompatible ingredients
    Given I am placing a new order
    When I select incompatible ingredients
    Then the system should reject my request
    And suggest valid combinations

  Scenario: Handle dietary restrictions
    Given I have specified I am vegan
    When I select non-vegan ingredients
    Then the system should reject my selection
    And suggest vegan alternatives

  Scenario: Manage allergies
    Given I have a peanut allergy
    When I select a meal containing peanuts
    Then the system should suggest an alternative ingredient