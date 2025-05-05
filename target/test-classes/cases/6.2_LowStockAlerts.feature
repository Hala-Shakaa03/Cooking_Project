Feature: Notify kitchen staff of low-stock ingredients
  As a kitchen manager
  I want to get alerts when stock is low
  So that I can reorder on time

  Scenario: Low stock alert
    Given stock levels are below the threshold
    When the system reviews inventory for low stock
    Then I should receive an alert to reorder ingredients
