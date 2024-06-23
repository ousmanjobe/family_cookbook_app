package com.example.jobe_oboly_a_finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameTextView, recipeDescriptionTextView, recipeDurationTextView, recipeIngredientsTextView, recipeInstructionsTextView;
    private ImageView recipeImageView;
    private Button favoriteButton, removeButton, returnButton, cameraButton;
    private Recipe recipe;
    private FirebaseAuth mAuth;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeNameTextView = findViewById(R.id.recipeNameTextView);
        recipeDescriptionTextView = findViewById(R.id.recipeDescriptionTextView);
        recipeDurationTextView = findViewById(R.id.recipeDurationTextView);
        recipeIngredientsTextView = findViewById(R.id.recipeIngredientsTextView);
        recipeInstructionsTextView = findViewById(R.id.recipeInstructionsTextView);
        recipeImageView = findViewById(R.id.recipeImageView);
        favoriteButton = findViewById(R.id.favoriteButton);
        removeButton = findViewById(R.id.removeButton);
        returnButton = findViewById(R.id.returnButton);
        cameraButton = findViewById(R.id.cameraButton);

        db = AppDatabase.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        // Retrieve recipe from intent
        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("recipe");

        displayRecipeDetails();

        cameraButton.setOnClickListener(v -> takePhoto());
        favoriteButton.setOnClickListener(v -> toggleFavorite());
        removeButton.setOnClickListener(v -> removeRecipe());
        returnButton.setOnClickListener(v -> returnToRecipeList());
    }

    private void displayRecipeDetails() {
        recipeNameTextView.setText(recipe.getName());
        recipeDescriptionTextView.setText(recipe.getDescription());
        recipeDurationTextView.setText(recipe.getDuration());
        recipeIngredientsTextView.setText(recipe.getIngredients());
        recipeInstructionsTextView.setText(recipe.getInstructions());
    }

    private void takePhoto() {
        // Implement camera functionality and upload photo
    }

    private void toggleFavorite() {
        recipe.setFavorite(!recipe.isFavorite());
        AsyncTask.execute(() -> db.recipeDao().updateRecipe(recipe.toEntity()));
        String message = recipe.isFavorite() ? "Recipe added to favorites" : "Recipe removed from favorites";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void removeRecipe() {
        AsyncTask.execute(() -> {
            db.recipeDao().deleteRecipe(recipe.toEntity());
            runOnUiThread(() -> {
                Toast.makeText(this, "Recipe removed", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        });
    }

    private void returnToRecipeList() {
        finish();
    }
}
