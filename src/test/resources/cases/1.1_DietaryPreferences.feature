
Feature: Store customer dietary preferences and allergies
  As a customer
  I want to input my dietary preferences and allergies
  So that the system can recommend appropriate meals and avoid allergens

  Scenario Outline: Customer inputs dietary preferences
    Given I am a registered customer
    When I add "<preference>" as my dietary preference and "<allergy>" as an allergy
    Then the system should save these preferences to my profile
    
 Examples: 
      | preference  | allergy     |
      | Vegan       | Peanuts     |
      | Vegetarian  | Shellfish   |
      | Gluten-Free | Dairy       |
      | Keto        | None        |

  Scenario: Chef views customer dietary preferences
    Given I am a chef
    When I open a customers profile
    Then I should see their dietary preferences and allergies