package com.example.yosef.bakingapp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yosef.bakingapp.R;
import com.example.yosef.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yosefricaro on 12/08/2017.
 */

public class IngredientViewAdapter extends RecyclerView.Adapter<IngredientViewAdapter.IngredientViewHolder>{
    private Context context;
    private List<Ingredient> ingredients;

    public IngredientViewAdapter(List<Ingredient> ingredients){
        this.ingredients = new ArrayList<>();
        this.ingredients.addAll(ingredients);
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_content, parent, false);
        IngredientViewHolder ingredientViewHolder = new IngredientViewHolder(view);
        return ingredientViewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        final Ingredient ingredient = ingredients.get(position);

        holder.ingredient.setText(ingredient.getQuantity()+" "+ingredient.getMeasure()+" "+ingredient.getIngredient());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ItemDetailActivity.class);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.list_step) TextView ingredient;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
