package com.cooking.logic;

import com.cooking.model.Recipe;
import java.util.List;

public class AIRecipeRecommender {

    public static String recommend(List<Recipe> recipes, List<String> ingredients, String preference, int time) {
        String bestMatch = null;
        int maxMatchingIngredients = 0;

        for (Recipe recipe : recipes) {
            if (!recipe.getDiet().equalsIgnoreCase(preference)) continue;
            if (recipe.getTime() > time) continue;

            int matchCount = 0;
            for (String ing : recipe.getIngredients()) {
                if (ingredients.contains(ing)) {
                    matchCount++;
                }
            }

            if (matchCount == recipe.getIngredients().size()) {
                return recipe.getName(); // perfect match
            }

            if (matchCount > maxMatchingIngredients) {
                maxMatchingIngredients = matchCount;
                bestMatch = recipe.getName();
            }
        }

        return bestMatch;
    }
}
