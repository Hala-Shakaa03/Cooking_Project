package com.cooking.core;

import java.util.ArrayList;
import java.util.List;

public class MealBuilder {
    private final List<String> ingredients = new ArrayList<>();

    public MealBuilder withIngredients(String... items) {
        ingredients.addAll(List.of(items));
        return this;
    }

    public List<String> build() {
        return new ArrayList<>(ingredients);
    }
}