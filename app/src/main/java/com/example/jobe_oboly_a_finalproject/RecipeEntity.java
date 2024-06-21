package com.example.jobe_oboly_a_finalproject;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class RecipeEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String description;
    private String duration;
    private String ingredients;
    private String instructions;
    private String imageUrl;

    public RecipeEntity(@NonNull String id, String name, String description, String duration, String ingredients, String instructions, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
}
