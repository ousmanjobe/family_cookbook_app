package com.example.jobe_oboly_a_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeListActivity extends AppCompatActivity {

    private EditText recipeNameEditText;
    private Button addRecipeButton;
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter adapter;
    private FirebaseFirestore db;
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
        adapter = new RecipeAdapter(recipes, this::onRecipeClick);
        recipesRecyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.homeButton).setOnClickListener(v -> startActivity(new Intent(RecipeListActivity.this, HomePageActivity.class)));
        findViewById(R.id.profileButton).setOnClickListener(v -> startActivity(new Intent(RecipeListActivity.this, ProfileActivity.class)));

        addRecipeButton.setOnClickListener(v -> showAddRecipeDialog());
        loadRecipes();
    }

    private void onRecipeClick(Recipe recipe){
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", (Serializable) recipe);
        startActivity(intent);
    }

    private void showAddRecipeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_recipe, null);
        EditText nameEditText = view.findViewById(R.id.recipeNameEditText);
        EditText descriptionEditText = view.findViewById(R.id.recipeDescriptionEditText);
        EditText durationEditText = view.findViewById(R.id.recipeDurationEditText);
        EditText ingredientsEditText = view.findViewById(R.id.recipeIngredientsEditText);
        EditText instructionsEditText = view.findViewById(R.id.recipeInstructionsEditText);

        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button confirmButton = view.findViewById(R.id.confirmButton);

        // Handle Cancel button
        cancelButton.setOnClickListener(v -> {
            // Dismiss the dialog
            builder.create().dismiss();
        });

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
                // Call method to add recipe to Firebase or Room database
                addRecipe(recipeName, description, duration, ingredients, instructions);

                // Dismiss the dialog
                builder.create().dismiss();
            } else {
                Toast.makeText(this, "Recipe name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        if (!recipeNameEditText.getText().toString().isEmpty()) {
            nameEditText.setText(recipeNameEditText.getText().toString());
        }

        builder.setView(view)
                .setTitle("Add Recipe")
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameEditText.getText().toString().trim();
                    String description = descriptionEditText.getText().toString().trim();
                    String duration = durationEditText.getText().toString().trim();
                    String ingredients = ingredientsEditText.getText().toString().trim();
                    String instructions = instructionsEditText.getText().toString().trim();

                    if (name.isEmpty() || description.isEmpty() || duration.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
                        Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addRecipe(name, description, duration, ingredients, instructions);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void addRecipe(String name, String description, String duration, String ingredients, String instructions) {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Recipe recipe = new Recipe(name, description, duration, ingredients, instructions);
        db.collection("users").document(userId).collection("recipes").add(recipe)
                .addOnSuccessListener(documentReference -> {
                    recipes.add(recipe);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to add recipe", Toast.LENGTH_SHORT).show());
    }

    private void loadRecipes() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users").document(userId).collection("recipes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            recipes.add(recipe);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}