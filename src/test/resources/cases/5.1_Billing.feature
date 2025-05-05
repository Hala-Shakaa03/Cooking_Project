Feature: Generate invoices and track financial performance
  As a customer
  I want to receive an invoice after my order
  So that I can have proof of payment

  Scenario: Customer receives invoice
    Given I have completed a meal order
    When the system processes the order
    Then I should receive an invoice via email or system notification

  Scenario: Admin generates financial reports
    Given multiple transactions have occurred
    When I request a financial report
    Then the system should generate a revenue and performance report