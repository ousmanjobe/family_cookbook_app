package com.example.jobe_oboly_a_finalproject;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private TextView usernameTextView;
    private Button preferencesButton, logoutButton, deleteAccountButton, homeButton, allRecipesButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.usernameTextView);
        preferencesButton = findViewById(R.id.preferencesButton);
        logoutButton = findViewById(R.id.logoutButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        homeButton = findViewById(R.id.homeButton);
        allRecipesButton = findViewById(R.id.allRecipesButton);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            usernameTextView.setText(user.getEmail());
        }

        preferencesButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, PreferencesActivity.class)));
        logoutButton.setOnClickListener(v -> logout());
        deleteAccountButton.setOnClickListener(v -> confirmDeleteAccount());
        homeButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, HomePageActivity.class)));
        allRecipesButton.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, RecipeListActivity.class)));
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).delete();
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}