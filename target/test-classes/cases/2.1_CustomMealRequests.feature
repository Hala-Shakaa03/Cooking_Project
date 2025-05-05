Feature: Allow customers to create custom meal requests
  As a customer
  I want to select ingredients and customize my meal
  So that I can personalize my order

  Scenario: Customer selects ingredients for a custom meal
    Given I am placing a new order
    When I select available ingredients
    Then the system should validate and accept my custom meal request

  Scenario: System validates ingredient combinations
    Given I select incompatible ingredients
    When I submit the custom meal request
    Then the system should display an error and suggest corrections