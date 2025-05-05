package com.cooking.AcceptanceTest;

import io.cucumber.java.en.*;

public class RecipeRecommendationSteps {

    @Given("I have selected {string} and available ingredients are Tomatoes, basil, pasta with {int} minutes available")
    public void iHaveSelectedAndAvailableIngredientsAreTomatoesBasilPastaWithMinutesAvailable(String preference, Integer time) {
        System.out.println("Selected: " + preference + ", Ingredients: Tomatoes, basil, pasta, Time: " + time + " minutes");
    }

    @Given("I have selected {string} and available ingredients are Basil, pasta, olive oil with {int} minutes available")
    public void iHaveSelectedAndAvailableIngredientsAreBasilPastaOliveOilWithMinutesAvailable(String preference, Integer time) {
        System.out.println("Selected: " + preference + ", Ingredients: Basil, pasta, olive oil, Time: " + time + " minutes");
    }

    @Given("I have selected {string} and available ingredients are Tomatoes, basil, garlic with {int} minutes available")
    public void iHaveSelectedAndAvailableIngredientsAreTomatoesBasilGarlicWithMinutesAvailable(String preference, Integer time) {
        System.out.println("Selected: " + preference + ", Ingredients: Tomatoes, basil, garlic, Time: " + time + " minutes");
    }

    @Given("I have selected {string} and available ingredients are Tomatoes, pasta with {int} minutes available")
    public void iHaveSelectedAndAvailableIngredientsAreTomatoesPastaWithMinutesAvailable(String preference, Integer time) {
        System.out.println("Selected: " + preference + ", Ingredients: Tomatoes, pasta, Time: " + time + " minutes");
    }
}
