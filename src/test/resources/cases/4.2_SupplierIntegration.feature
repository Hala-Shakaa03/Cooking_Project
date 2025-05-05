Feature: Integrate supplier pricing and auto-purchase
  As a kitchen manager
  I want real-time supplier data
  So that I can make informed purchasing decisions

  Scenario: Fetch real-time pricing
    Given I need to purchase ingredients
    When I open supplier options
    Then I should see the latest prices

  Scenario: Auto-generate purchase order
    Given stock level is critically low
    When the system checks inventory
    Then it should automatically create a purchase order