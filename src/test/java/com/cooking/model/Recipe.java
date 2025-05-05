package com.cooking.model;

import java.util.List;

public class Recipe {
    private String name;
    private List<String> ingredients;
    private int time;
    private String diet;

    public Recipe(String name, List<String> ingredients, int time, String diet) {
        this.name = name;
        this.ingredients = ingredients;
        this.time = time;
        this.diet = diet;
    }

    public String getName() { return name; }
    public List<String> getIngredients() { return ingredients; }
    public int getTime() { return time; }
    public String getDiet() { return diet; }
}
