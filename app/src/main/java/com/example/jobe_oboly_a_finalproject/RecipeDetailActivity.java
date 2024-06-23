package com.example.jobe_oboly_a_finalproject;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.Objects;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    private TextView recipeNameTextView, recipeDescriptionTextView, recipeDurationTextView, recipeIngredientsTextView, recipeInstructionsTextView;
    private ImageView recipeImageView;
    private Button favoriteButton, removeButton, returnButton, cameraButton;
    private Recipe recipe;
    private FirebaseAuth mAuth;
    private AppDatabase db;

    private Uri photoURI;

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

        cameraButton.setOnClickListener(v -> checkCameraPermission());
        favoriteButton.setOnClickListener(v -> toggleFavorite());
        removeButton.setOnClickListener(v -> removeRecipe());
        returnButton.setOnClickListener(v -> returnToRecipeList());

        updateFavoriteButton();
    }

    private void displayRecipeDetails() {
        recipeNameTextView.setText(recipe.getName());
        recipeDescriptionTextView.setText(recipe.getDescription());
        recipeDurationTextView.setText(recipe.getDuration());
        recipeIngredientsTextView.setText(recipe.getIngredients());
        recipeInstructionsTextView.setText(recipe.getInstructions());

        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            recipeImageView.setImageURI(Uri.parse(recipe.getImageUrl()));
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        photoURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (photoURI != null) {
                recipeImageView.setImageURI(photoURI);
                recipe.setImageUrl(photoURI.toString());

                AsyncTask.execute(() -> db.recipeDao().updateRecipe(recipe.toEntity()));
                Toast.makeText(this, "Photo saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toggleFavorite() {
        recipe.setFavorite(!recipe.isFavorite());
        AsyncTask.execute(() -> db.recipeDao().updateRecipe(recipe.toEntity()));
        String message = recipe.isFavorite() ? "Recipe added to favorites" : "Recipe removed from favorites";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        updateFavoriteButton();
        setResult(RESULT_OK); // Notify HomePageActivity of the change
    }

    private void updateFavoriteButton() {
        favoriteButton.setText(recipe.isFavorite() ? "Unfavorite" : "Favorite");
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
