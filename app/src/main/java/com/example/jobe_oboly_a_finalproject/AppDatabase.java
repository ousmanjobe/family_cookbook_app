package com.example.jobe_oboly_a_finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecipeEntity.class, UserPreferencesEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract UserPreferencesDao userPreferencesDao();
}
