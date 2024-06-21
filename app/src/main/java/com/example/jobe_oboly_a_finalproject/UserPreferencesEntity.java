package com.example.jobe_oboly_a_finalproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_preferences")
public class UserPreferencesEntity {
    @PrimaryKey
    private int id;
    private boolean isDarkTheme;

    public UserPreferencesEntity(int id, boolean isDarkTheme) {
        this.id = id;
        this.isDarkTheme = isDarkTheme;
    }

    // Getters and setters
}
