package com.example.yosef.bakingapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yosef.bakingapp.R;
import com.example.yosef.bakingapp.model.Ingredient;
import com.example.yosef.bakingapp.model.StaticRecipe;
import com.example.yosef.bakingapp.model.Step;
import com.example.yosef.bakingapp.view.adapter.IngredientViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        List<Ingredient> li = StaticRecipe.getRecipe().getIngredients();
        List<String> ls = new ArrayList<>();
        for(int i = 0; i<li.size(); i++){
            ls.add(li.get(i).getQuantity()+" "+li.get(i).getMeasure()+" "+li.get(i).getIngredient());
        }
        final RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.rv_ingredient);
        IngredientViewAdapter rcAdapter = new IngredientViewAdapter(li);
        recyclerView.setAdapter(rcAdapter);

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_list_item_1,
//                ls);
//
//        lv.setAdapter(arrayAdapter);

        List<Step> lstep = StaticRecipe.getRecipe().getSteps();
        View recyclerView2 = findViewById(R.id.item_list);
        assert recyclerView2 != null;
        setupRecyclerView((RecyclerView) recyclerView2, lstep);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Step> ls) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(ls));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Step> mValues;

        public SimpleItemRecyclerViewAdapter(List<Step> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_cardview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final String title = mValues.get(position).getId()+". "+mValues.get(position).getShortDescription();
            holder.list_step.setText(title);

            if (mValues.get(position).getThumbnailURL().isEmpty()) {
                holder.step_image.setImageResource(R.mipmap.ic_launcher);
            } else{
                Picasso.with(ItemListActivity.this)
                        .load(mValues.get(position).getThumbnailURL())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.step_image);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(ItemDetailFragment.ARG_ITEM_ID, position);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra("id", position);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.list_step) TextView list_step;
            @BindView(R.id.step_image) ImageView step_image;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
