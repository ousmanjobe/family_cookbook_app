package com.example.jobe_oboly_a_finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePageActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RECIPE_DETAIL = 1;

    private RecyclerView favoritesRecyclerView;
    private RecipeAdapter adapter;
    private FirebaseAuth mAuth;
    private List<Recipe> favoriteRecipes;

    private AppDatabase roomDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteRecipes = new ArrayList<>();
        adapter = new RecipeAdapter(favoriteRecipes, this::onRecipeClick, this::onRecipeLongClick);
        favoritesRecyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        roomDatabase = AppDatabase.getInstance(this);

        findViewById(R.id.allRecipesButton).setOnClickListener(v -> startActivity(new Intent(HomePageActivity.this, RecipeListActivity.class)));
        findViewById(R.id.profileButton).setOnClickListener(v -> startActivity(new Intent(HomePageActivity.this, ProfileActivity.class)));

        loadFavoriteRecipes();
    }

    private void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", (Serializable) recipe);
        startActivityForResult(intent, REQUEST_CODE_RECIPE_DETAIL);
    }

    private void onRecipeLongClick(Recipe recipe) {
        showRemoveRecipeDialog(recipe);
    }

    private void showRemoveRecipeDialog(Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Recipe")
                .setMessage("Are you sure you want to remove this recipe?")
                .setPositiveButton("Remove", (dialog, which) -> removeRecipe(recipe))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void removeRecipe(Recipe recipe) {
        AsyncTask.execute(() -> {
            roomDatabase.recipeDao().deleteRecipe(recipe.toEntity());
            loadFavoriteRecipes();
        });
    }

    private void loadFavoriteRecipes() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        AsyncTask.execute(() -> {
            List<RecipeEntity> recipeEntities = roomDatabase.recipeDao().getFavoriteRecipes(userId);
            runOnUiThread(() -> {
                favoriteRecipes.clear();
                for (RecipeEntity entity : recipeEntities) {
                    favoriteRecipes.add(Recipe.fromEntity(entity));
                }
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECIPE_DETAIL && resultCode == RESULT_OK) {
            loadFavoriteRecipes();
        }
    }
}
