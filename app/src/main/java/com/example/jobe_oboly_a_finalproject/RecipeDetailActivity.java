package com.example.jobe_oboly_a_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameTextView, recipeDescriptionTextView, recipeDurationTextView, recipeIngredientsTextView, recipeInstructionsTextView;
    private ImageView recipeImageView;
    private Button favoriteButton, removeButton, returnButton, cameraButton;
    private Recipe recipe;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

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

        db = FirebaseFirestore.getInstance();
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
        // Implement favorite/unfavorite functionality
    }

    private void removeRecipe() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users").document(userId).collection("recipes").document(recipe.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Recipe removed", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove recipe", Toast.LENGTH_SHORT).show());
    }

    private void returnToRecipeList() {
        finish();
    }
}