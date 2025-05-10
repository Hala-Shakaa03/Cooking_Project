Feature: Integrate supplier pricing and auto-purchase
  As a kitchen manager
  I want to integrate with supplier systems
  So that I can optimize purchasing and maintain stock

  Scenario: Fetch real-time ingredient prices
    Given the following suppliers are available:
      | Supplier   | API Endpoint                      |
      | FreshFoods | https://api.freshfoods.com/prices |
      | Metro      | https://api.metro.com/inventory   |
    When I request current prices for "Saffron"
    Then I should receive prices from all suppliers
    And the system should cache prices for 1 hour

  Scenario: Auto-generate purchase order for critically low stock
  Given "Saffron" has the following stock levels:
  | Field        | Value    |
  | currentStock | 2 grams  |
  | minimumLevel | 10 grams |
  And the best current price is from "FreshFoods" at $5.00 per gram
  When the automatic inventory check runs
  Then the system should generate a purchase order for 20 grams of "Saffron"
  And the PO should be sent to "FreshFoods"
  And the PO total should be $100.00
    