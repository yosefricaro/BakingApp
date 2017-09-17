package com.example.yosef.bakingapp.util;

import com.example.yosef.bakingapp.BuildConfig;
import com.example.yosef.bakingapp.model.RecipeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yosefricaro on 03/08/2017.
 */

public class RetrofitUtil {
    private static Retrofit getRetrofit(){

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.URI)
                .addConverterFactory(GsonConverterFactory.create(RetrofitUtil.getGson()))
                .build();
    }

    public static RecipeService getRecipeService(){
        return RetrofitUtil.getRetrofit().create(RecipeService.class);
    }

    private static Gson getGson(){
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    public static String toJson(Object object) {
        return RetrofitUtil.getGson().toJson(object);
    }
}
