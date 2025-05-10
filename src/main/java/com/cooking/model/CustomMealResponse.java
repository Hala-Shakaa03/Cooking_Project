package com.cooking.model;

import java.util.ArrayList;
import java.util.List;

public class CustomMealResponse {
    private boolean valid;
    private List<String> errors;
    private List<String> suggestions;

    public CustomMealResponse(boolean valid, List<String> errors, List<String> suggestions) {
        this.valid = valid;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
        this.suggestions = suggestions != null ? new ArrayList<>(suggestions) : new ArrayList<>();
    }

    // Getters
    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }
}