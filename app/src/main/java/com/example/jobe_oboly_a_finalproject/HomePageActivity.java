package com.example.jobe_oboly_a_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class HomePageActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private RecipeAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Recipe> favoriteRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteRecipes = new ArrayList<>();
        adapter = new RecipeAdapter(favoriteRecipes, this::onRecipeClick);
        favoritesRecyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.allRecipesButton).setOnClickListener(v -> startActivity(new Intent(HomePageActivity.this, RecipeListActivity.class)));
        findViewById(R.id.profileButton).setOnClickListener(v -> startActivity(new Intent(HomePageActivity.this, ProfileActivity.class)));

        loadFavoriteRecipes();
    }


    private void onRecipeClick(Recipe recipe){
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("recipe", (Serializable) recipe);
        startActivity(intent);
    }
    private void loadFavoriteRecipes() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users").document(userId).collection("favorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            favoriteRecipes.add(recipe);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}