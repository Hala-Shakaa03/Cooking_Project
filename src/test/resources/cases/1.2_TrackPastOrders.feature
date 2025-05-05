Feature: Track customer past orders and personalized meal plans
  As a customer
  I want to view my past meal orders
  So that I can reorder meals I liked

  Scenario: Customer views past orders
    Given I have placed previous orders
    When I visit my order history
    Then I should see a list of my past orders

  Scenario: Chef views customer order history
    Given a customer has previous orders
    When I view the customers profile
    Then I should see their past orders to suggest meal plans

  Scenario: Admin analyzes order trends
    Given the system has multiple customers with order history
    When I generate a trend report
    Then I should see popular meals and customer preferences




