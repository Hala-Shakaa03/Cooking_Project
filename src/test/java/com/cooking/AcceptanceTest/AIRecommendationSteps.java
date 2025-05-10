/*package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;
import java.util.*;
import com.cooking.model.Recipe;
import com.cooking.logic.AIRecipeRecommender;

public class AIRecommendationSteps {

    private String dietaryPreference;
    private int availableTime;
    private List<String> availableIngredients = new ArrayList<>();
    private String recommendedRecipe;

    private static final List<Recipe> recipes = Arrays.asList(
        new Recipe("Spaghetti with Tomato Sauce", Arrays.asList("pasta", "tomato", "garlic"), 30, "Vegan"),
        new Recipe("Grilled Chicken Salad", Arrays.asList("chicken", "lettuce", "tomato", "olive oil"), 25, "Low-Carb"),
        new Recipe("Vegan Pesto Pasta", Arrays.asList("pasta", "basil", "olive oil", "nuts"), 20, "Vegan"),
        new Recipe("Tomato Basil Soup", Arrays.asList("tomato", "basil", "garlic"), 15, "Vegan")
    );

    @Given("the customer has a {string} dietary preference")
    public void the_customer_has_a_dietary_preference(String preference) {
        this.dietaryPreference = preference;
    }

    @And("they have a maximum preparation time of {int} minutes")
    public void they_have_a_maximum_preparation_time_of_minutes(int time) {
        this.availableTime = time;
    }

    @And("they have the following ingredients: {string}")
    public void they_have_the_following_ingredients(String ingredients) {
        this.availableIngredients = (ingredients == null || ingredients.isEmpty())
            ? new ArrayList<>()
            : Arrays.asList(ingredients.toLowerCase().split(",\\s*"));
    }

    @When("I request a recipe recommendation")
    public void i_request_a_recipe_recommendation() {
        recommendedRecipe = AIRecipeRecommender.recommend(recipes, availableIngredients, dietaryPreference, availableTime);
    }

    @Then("the system should suggest {string} because {string}")
    public void theSystemShouldSuggestBecause(String expectedRecipe, String reason) {
        if (recommendedRecipe == null) {
            throw new AssertionError("No recipe was recommended.");
        }
        if (!recommendedRecipe.equalsIgnoreCase(expectedRecipe)) {
            throw new AssertionError("Expected: " + expectedRecipe + ", but got: " + recommendedRecipe);
        }
    }
}*/
