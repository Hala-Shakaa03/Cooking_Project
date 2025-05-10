Feature: Track available ingredients and suggest restocking
  As a kitchen manager
  I want to track ingredient stock levels and get restocking suggestions
  So that I can prevent shortages and ensure continuous operations

Scenario: Monitor ingredient stock levels and show alerts when below minimum
    Given the system has ingredients with defined minimum stock levels
    When the current stock of "Saffron" falls below its minimum level of 10 grams
    Then the system should show a restocking alert for "Saffron"
    And the alert should indicate the current stock level is critical

  Scenario: System suggests restocking when daily check finds low inventory
    Given the following ingredients are below their minimum levels:
      | Name    | Current Stock | Minimum Level |
      | Saffron | 5 grams       | 10 grams      |
      | Basil   | 20 grams      | 50 grams      |
   When the system performs its daily stock check
   Then the system should generate restock requests for:
      | Name    | Quantity Needed |
      | Saffron | 5 grams         |  
      | Basil   | 30 grams        |

  Scenario: Critical stock items appear first in restocking priorities
    Given the following ingredients need replenishment:
      | Name     | Current Stock | Minimum Level | Critical Threshold |
      | Saffron  | 2 grams       | 10 grams      | 20%                |
      | Basil    | 20 grams      | 50 grams      | 30%                |
    When the system evaluates inventory priorities
    Then "Saffron" should be marked as urgent
    And "Saffron" should appear first in the restocking list
    And "Basil" should appear second in the restocking list