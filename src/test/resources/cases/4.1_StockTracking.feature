Feature: Track available ingredients and suggest restocking
  As a kitchen manager
  I want to monitor stock levels
  So that I can prevent shortages

  Scenario: Monitor ingredient stock
    Given I am managing inventory
    When stock levels drop below threshold
    Then the system should alert for restocking

  Scenario: Automatic restocking suggestion
    Given ingredient stock is low
    When the system checks inventory
    Then it should generate a restock request