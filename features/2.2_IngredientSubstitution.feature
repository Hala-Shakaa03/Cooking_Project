 Feature: Suggest ingredient substitutions for dietary restrictions
  As a customer
  I want the system to suggest alternatives
  So that I can still enjoy meals safely
  
 Scenario: Suggest substitute for unavailable ingredient
    Given the ingredient "Cheese" is unavailable
    When the system checks for substitutes
    Then it should suggest "Nutritional yeast" as a replacement
    And notify the assigned chef

  Scenario: Chef reviews and approves substitution
    Given the system suggested "Almond milk" to replace "Milk"
    When the chef "chef@example.com" receives the alert
    Then the chef should be able to:
      | Action          | Outcome                          |
      | Approve         | Substitution is applied          |
      | Modify          | Suggest a different substitute   |

  Scenario: Substitute for allergy (dairy-free)
    Given the customer has a "Dairy" allergy
    And selected "Butter" in their recipe
    When the system suggests substitutes
    Then it should only show dairy-free options like "Coconut oil"
    And validate compatibility with the recipe

  Scenario: Multiple valid substitutes
    Given the ingredient "Egg" is unavailable
    And the customer has no dietary restrictions
    When the system searches for substitutes
    Then it should display all options:
      | Flaxseed meal |
      | Applesauce    |
      | Chia seeds    |