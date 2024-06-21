package com.example.jobe_oboly_a_finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PreferencesActivity extends AppCompatActivity {

    private Switch themeSwitch;
    private Button returnButton;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preferences);

        themeSwitch = findViewById(R.id.themeSwitch);
        returnButton = findViewById(R.id.returnButton);

        preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        themeSwitch.setChecked(preferences.getBoolean("dark_theme", false));
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("dark_theme", isChecked);
            editor.apply();
            updateTheme();
        });

        returnButton.setOnClickListener(v -> finish());
    }

    private void updateTheme() {
        if (preferences.getBoolean("dark_theme", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}