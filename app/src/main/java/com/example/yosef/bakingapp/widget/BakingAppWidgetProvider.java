package com.example.yosef.bakingapp.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.yosef.bakingapp.R;
import com.example.yosef.bakingapp.model.Recipe;
import com.example.yosef.bakingapp.model.StaticRecipe;
import com.google.gson.GsonBuilder;

/**
 * Created by iip on 8/10/17.
 */

public class BakingAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);
            appWidgetManager.updateAppWidget(widgetId, mView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.test_layout);

        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        Recipe recipe = StaticRecipe.getRecipe();
        String sRecipe = new GsonBuilder().create().toJson(recipe);

        intent.putExtra(BakingAppDataProvider.SELECTED_RECIPE, sRecipe);
        mView.setRemoteAdapter(widgetId, R.id.list, intent);

        return mView;
    }
}
