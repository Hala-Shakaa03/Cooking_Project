Feature: Send reminders for upcoming orders and deliveries
  As a customer
  I want to receive reminders before meal deliveries
  So that I can be prepared
  And the chef can manage cooking tasks efficiently

  Scenario: Customer receives delivery reminder
    Given I am a registered customer with an upcoming order
    When the scheduled delivery time is approaching
    Then I should receive a delivery notification

  Scenario: Chef receives cooking task reminder
    Given the chef has scheduled cooking tasks
    When the task time is near
    Then the system should send a cooking task reminder
