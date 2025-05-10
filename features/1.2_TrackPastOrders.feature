Feature: Track customer past orders and personalized meal plans
  As a customer
  I want to view my past meal orders
  So that I can reorder meals I liked

   Scenario Outline: Customer views order history
    Given customer "<ID>" has <count> past orders
    When <role> views the order history
    Then they should see <expected> orders
    And the response status should be <status>

    Examples:
      | ID    | count | role      | expected | status  |
      | C-101 | 3     | customer  | 3        | success |
      | C-102 | 0     | customer  | 0        | success |
      | C-103 | 5     | chef      | 5        | success |
      | C-104 | 2     | admin     | 2        | success |
      | C-105 | 1     | unauthorized user  | 0        | denied  |