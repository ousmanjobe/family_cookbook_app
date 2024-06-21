package com.example.jobe_oboly_a_finalproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    UserPreferencesEntity getUserPreferences();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserPreferences(UserPreferencesEntity preferences);
}
