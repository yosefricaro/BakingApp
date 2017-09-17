package com.example.yosef.bakingapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yosef.bakingapp.R;
import com.example.yosef.bakingapp.model.Recipe;
import com.example.yosef.bakingapp.util.RetrofitUtil;
import com.example.yosef.bakingapp.view.adapter.RecipeViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllItemList();

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshContainer);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllItemList();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAllItemList();
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    public void getAllItemList(){
        final ProgressBar mLoading = (ProgressBar) findViewById(R.id.progress_bar);
        mLoading.setVisibility(View.VISIBLE);

        GridLayoutManager gridLayout;
        gridLayout = new GridLayoutManager(MainActivity.this, 1);

        final RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        final List<Recipe> lr = new ArrayList<>();
        RetrofitUtil.getRecipeService().getRecipe().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> lr = response.body();
                getResponse(recyclerView, lr, mLoading);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Tidak ada internet", Toast.LENGTH_SHORT).show();
                Log.e("error", t.getMessage());
            }
        });
        getResponse(recyclerView, lr, mLoading);
    }

    public void getResponse(RecyclerView recyclerView, List<Recipe> lr, ProgressBar mLoading){
        RecipeViewAdapter rcAdapter = new RecipeViewAdapter(lr);
        recyclerView.setAdapter(rcAdapter);
        mLoading.setVisibility(View.INVISIBLE);
    }
}
