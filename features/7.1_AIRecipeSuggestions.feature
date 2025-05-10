@ignore
 Feature: Recommend recipes using AI assistant
  As a customer
  I want recipe recommendations based on my preferences
  So that I can choose a suitable meal easily

  Scenario Outline: Recommend a recipe based on preferences, ingredients, and time
    Given the customer has a "<preference>" dietary preference
    And they have a maximum preparation time of <time> minutes
    And they have the following ingredients: "<ingredients>"
    When I request a recipe recommendation
    Then the system should suggest "<expectedRecipe>" because "<explanation>"

  Examples:
    | preference | ingredients                 | time | expectedRecipe              | explanation                                                                 |
    | Vegan      | tomato, basil, pasta        | 30   | Spaghetti with Tomato Sauce | It uses all provided ingredients, fits within 30 mins, and meets Vegan diet |
    | Vegan      | basil, pasta, olive oil     | 20   | Vegan Pesto Pasta           | It uses available ingredients and is the quickest vegan option               |
    | Vegan      | tomato, basil, garlic       | 40   | Tomato Basil Soup           | It matches all ingredients and fits the exact time available                 |
    | Vegan      | Tomatoes, pasta             | 30   | Spaghetti with Tomato Sauce | It uses most ingredients and fits the Vegan requirement                      |
