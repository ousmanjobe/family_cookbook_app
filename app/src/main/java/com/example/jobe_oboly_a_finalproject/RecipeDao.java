package com.example.jobe_oboly_a_finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insertRecipe(RecipeEntity recipe);

    @Update
    void updateRecipe(RecipeEntity recipe);

    @Delete
    void deleteRecipe(RecipeEntity recipe);

    @Query("SELECT * FROM recipes WHERE userId = :userId")
    List<RecipeEntity> getAllRecipes(String userId);

    @Query("SELECT * FROM recipes WHERE id = :id")
    RecipeEntity getRecipeById(String id);

    @Query("SELECT * FROM recipes WHERE userId = :userId AND isFavorite = 1")
    List<RecipeEntity> getFavoriteRecipes(String userId);
}
