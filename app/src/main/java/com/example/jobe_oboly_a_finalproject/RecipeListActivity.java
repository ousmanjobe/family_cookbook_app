package com.example.jobe_oboly_a_finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.UUID;

public class RecipeListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RECIPE_DETAIL = 1;

    private EditText recipeNameEditText;
    private Button addRecipeButton;
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter adapter;
    private AppDatabase db;
    private FirebaseAuth mAuth;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recipeNameEditText = findViewById(R.id.recipeNameEditText);
        addRecipeButton = findViewById(R.id.addRecipeButton);
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipes = new ArrayList<>();
        adapter = new RecipeAdapter(recipes, this::onRecipeClick, this::onRecipeLongClick);
        recipesRecyclerView.setAdapter(adapter);
        db = AppDatabase.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.homeButton).setOnClickListener(v -> startActivity(new Intent(RecipeListActivity.this, HomePageActivity.class)));
        findViewById(R.id.profileButton).setOnClickListener(v -> startActivity(new Intent(RecipeListActivity.this, ProfileActivity.class)));

        addRecipeButton.setOnClickListener(v -> showAddRecipeDialog());
        loadRecipes();
    }

    private void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", (Serializable) recipe);
        startActivityForResult(intent, REQUEST_CODE_RECIPE_DETAIL);
    }

    private void onRecipeLongClick(Recipe recipe) {
        // Handle long click action
    }

    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_recipe, null);
        EditText nameEditText = view.findViewById(R.id.recipeNameEditText);
        EditText descriptionEditText = view.findViewById(R.id.recipeDescriptionEditText);
        EditText durationEditText = view.findViewById(R.id.recipeDurationEditText);
        EditText ingredientsEditText = view.findViewById(R.id.recipeIngredientsEditText);
        EditText instructionsEditText = view.findViewById(R.id.recipeInstructionsEditText);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // Handle Confirm button
        confirmButton.setOnClickListener(v -> {
            // Retrieve text from EditText fields
            String recipeName = nameEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String duration = durationEditText.getText().toString().trim();
            String ingredients = ingredientsEditText.getText().toString().trim();
            String instructions = instructionsEditText.getText().toString().trim();

            // Validate input (example: check if recipeName is not empty)
            if (!TextUtils.isEmpty(recipeName)) {
                // Call method to add recipe to Room database
                addRecipe(recipeName, description, duration, ingredients, instructions);
                // Dismiss the dialog
                builder.create().dismiss();
            } else {
                Toast.makeText(this, "Recipe name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Cancel button
        cancelButton.setOnClickListener(v -> builder.create().dismiss());

        builder.setView(view)
                .setTitle("Add Recipe")
                .create()
                .show();
    }

    private void addRecipe(String name, String description, String duration, String ingredients, String instructions) {
        Recipe recipe = new Recipe(
                UUID.randomUUID().toString(),
                name,
                description,
                duration,
                ingredients,
                instructions,
                "",
                false,
                false,
                0L,
                Objects.requireNonNull(mAuth.getCurrentUser()).getUid()
        );

        AsyncTask.execute(() -> {
            db.recipeDao().insertRecipe(recipe.toEntity());
            loadRecipes();
        });
    }

    private void loadRecipes() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        AsyncTask.execute(() -> {
            List<RecipeEntity> recipeEntities = db.recipeDao().getAllRecipes(userId);
            runOnUiThread(() -> {
                recipes.clear();
                for (RecipeEntity entity : recipeEntities) {
                    recipes.add(Recipe.fromEntity(entity));
                }
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECIPE_DETAIL && resultCode == RESULT_OK) {
            loadRecipes();
        }
    }
}
