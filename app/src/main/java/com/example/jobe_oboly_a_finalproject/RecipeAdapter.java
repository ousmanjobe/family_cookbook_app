package com.example.jobe_oboly_a_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private OnRecipeClickListener clickListener;
    private OnRecipeLongClickListener longClickListener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public interface OnRecipeLongClickListener {
        void onRecipeLongClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener clickListener, OnRecipeLongClickListener longClickListener) {
        this.recipes = recipes;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeNameTextView, recipeDurationTextView;
        private ImageView favoriteIconImageView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.recipeNameTextView);
            recipeDurationTextView = itemView.findViewById(R.id.recipeDurationTextView);
            favoriteIconImageView = itemView.findViewById(R.id.favoriteIconImageView);
        }

        public void bind(Recipe recipe, OnRecipeClickListener clickListener, OnRecipeLongClickListener longClickListener) {
            recipeNameTextView.setText(recipe.getName());
            recipeDurationTextView.setText(recipe.getDuration());
            favoriteIconImageView.setVisibility(recipe.isFavorite() ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> clickListener.onRecipeClick(recipe));
            itemView.setOnLongClickListener(v -> {
                longClickListener.onRecipeLongClick(recipe);
                return true;
            });
        }
    }
}
