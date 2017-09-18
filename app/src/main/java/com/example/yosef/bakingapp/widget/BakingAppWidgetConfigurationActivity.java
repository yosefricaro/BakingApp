package com.example.yosef.bakingapp.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yosef.bakingapp.R;
import com.example.yosef.bakingapp.model.Recipe;
import com.example.yosef.bakingapp.model.StaticRecipe;
import com.example.yosef.bakingapp.util.RetrofitUtil;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iip on 8/12/17.
 */

public class BakingAppWidgetConfigurationActivity extends AppCompatActivity {

    private int mAppWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        setContentView(R.layout.activity_widget);

        final ProgressBar mLoading = (ProgressBar) findViewById(R.id.progress_bar);
        mLoading.setVisibility(View.VISIBLE);


        final RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view2);
        final List<Recipe> lr = new ArrayList<>();
        RetrofitUtil.getRecipeService().getRecipe().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> lr = response.body();
                getResponse(rv, lr, mLoading);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(BakingAppWidgetConfigurationActivity.this, "Tidak ada internet", Toast.LENGTH_SHORT).show();
                Log.e("error", t.getMessage());
            }
        });
        getResponse(rv, lr, mLoading);
    }

    public void getResponse(RecyclerView recyclerView, List<Recipe> lr, ProgressBar mLoading){
        RecyclerViewWidgetAdapter rcAdapter = new RecyclerViewWidgetAdapter(lr);
        recyclerView.setAdapter(rcAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLoading.setVisibility(View.INVISIBLE);
    }

    private void execute(Recipe recipe){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = initViews(this, appWidgetManager, mAppWidgetId, recipe);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent(this, BakingAppWidgetService.class);
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        setResult(RESULT_OK, resultValue);

        finish();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId, Recipe recipe) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.collection_widget);

        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        String sRecipe = new GsonBuilder().create().toJson(recipe);

        intent.putExtra(BakingAppDataProvider.SELECTED_RECIPE, sRecipe);
        mView.setRemoteAdapter(widgetId, R.id.widget_list, intent);

        return mView;
    }


    class RecyclerViewWidgetAdapter extends RecyclerView.Adapter<RecyclerViewWidgetAdapter.WidgetAdapter>{

        private Context context;
        List<Recipe> recipes = new ArrayList<>();

        public RecyclerViewWidgetAdapter(List<Recipe> recipes){
            this.recipes = recipes;
        }

        @Override
        public WidgetAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            WidgetAdapter widgetAdapter = new WidgetAdapter(view);
            return widgetAdapter ;
        }

        @Override
        public void onBindViewHolder(WidgetAdapter holder, int position) {
            final Recipe recipe = recipes.get(position);
            holder.tv.setText(recipe.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StaticRecipe.setRecipe(recipe);
                    execute(recipe);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (recipes != null)
            return recipes.size();
            else return 0;
        }

        class WidgetAdapter extends RecyclerView.ViewHolder{
            @BindView(android.R.id.text1) TextView tv;
            public WidgetAdapter(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

}
