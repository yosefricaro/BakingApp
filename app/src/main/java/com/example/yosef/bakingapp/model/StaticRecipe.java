package com.example.yosef.bakingapp.model;

/**
 * Created by Yosefricaro on 18/08/2017.
 */

public class StaticRecipe {
    private static Recipe Recipe;

    public static com.example.yosef.bakingapp.model.Recipe getRecipe() {
        return Recipe;
    }

    public static void setRecipe(com.example.yosef.bakingapp.model.Recipe recipe) {
        Recipe = recipe;
    }
}
