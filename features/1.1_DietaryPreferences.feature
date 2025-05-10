Feature: Store customer dietary preferences and allergies
  As a customer
  I want to input my dietary preferences and allergies
  So that the system can recommend appropriate meals and avoid allergens

  Scenario Outline: Customer saves valid dietary preferences and allergies
    Given I am a registered customer
    When I set dietary preferences to "<preference>" and allergy to "<allergy>"
    Then the system should save them exactly
    And the saved data should match "<preference>" and "<allergy>"

    Examples:
      | preference             | allergy       |
      | Vegan                  | Peanuts       |
      | Gluten-Free            | Dairy         |
      | Keto                   | None          |
      | Vegan, Gluten-Free     | Shellfish     |
      | Lacto-Ovo Vegetarian   | Tree Nuts     |

  Scenario: Authorized chef views customer profile
    Given a customer has saved preferences "Vegetarian" and allergy "Dairy"
    And I am a chef
    When I view the customer's profile
    Then I should see their dietary preferences and allergies
    And the displayed preferences should be "Vegetarian"
    And the displayed allergies should be "Dairy"

  Scenario: Unauthorized profile access
    Given a customer exists in the system
    And I am not authorized
    When I try to view a customer's preferences
    Then I should receive "Access denied"

  Scenario: Profile save during system outage
    Given I am a registered customer
    And the database is unavailable
    When I try to save new preferences "Paleo" and allergy "None"
    Then I should receive "System unavailable"
    And the system should not have the new preferences