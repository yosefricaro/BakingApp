package com.example.yosef.bakingapp.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Yosefricaro on 12/08/2017.
 */

public interface RecipeService {
    @GET("topher/2017/May/59121517_baking/baking.json") Call<List<Recipe>> getRecipe();
}
