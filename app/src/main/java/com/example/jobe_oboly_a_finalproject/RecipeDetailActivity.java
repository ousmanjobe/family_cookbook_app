package com.example.jobe_oboly_a_finalproject;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameTextView, recipeDescriptionTextView, recipeDurationTextView, recipeIngredientsTextView, recipeInstructionsTextView;
    private ImageView recipeImageView;
    private Button favoriteButton, removeButton, returnButton, cameraButton, shareButton;
    private Recipe recipe;
    private FirebaseAuth mAuth;
    private AppDatabase db;
    private Uri photoUri;

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
        shareButton = findViewById(R.id.shareButton);

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
        shareButton.setOnClickListener(v -> shareRecipe());
    }

    private void displayRecipeDetails() {
        recipeNameTextView.setText(recipe.getName());
        recipeDescriptionTextView.setText(recipe.getDescription());
        recipeDurationTextView.setText(recipe.getDuration());
        recipeIngredientsTextView.setText(recipe.getIngredients());
        recipeInstructionsTextView.setText(recipe.getInstructions());

        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            recipeImageView.setVisibility(View.VISIBLE);
            recipeImageView.setImageURI(Uri.parse(recipe.getImageUrl()));
        } else {
            recipeImageView.setVisibility(View.GONE);
        }

        updateFavoriteButton();
    }

    private void updateFavoriteButton() {
        favoriteButton.setText(recipe.isFavorite() ? "Unfavorite" : "Favorite");
    }

    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraLauncher.launch(intent);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        recipeImageView.setImageBitmap(bitmap);
                        recipeImageView.setVisibility(View.VISIBLE);
                        recipe.setImageUrl(photoUri.toString());
                        AsyncTask.execute(() -> db.recipeDao().updateRecipe(recipe.toEntity()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private void toggleFavorite() {
        recipe.setFavorite(!recipe.isFavorite());
        AsyncTask.execute(() -> {
            db.recipeDao().updateRecipe(recipe.toEntity());
            runOnUiThread(this::updateFavoriteButton);
        });
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

    private void shareRecipe() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String shareSubject = "Check out this recipe: " + recipe.getName();
        String shareBody = "Recipe Name: " + recipe.getName() + "\n\n"
                + "Description: " + recipe.getDescription() + "\n\n"
                + "Duration: " + recipe.getDuration() + "\n\n"
                + "Ingredients: " + recipe.getIngredients() + "\n\n"
                + "Instructions: " + recipe.getInstructions();

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(recipe.getImageUrl()));
        }

        startActivity(Intent.createChooser(shareIntent, "Share Recipe"));
    }
}
