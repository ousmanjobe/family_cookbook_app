package com.example.jobe_oboly_a_finalproject;

public class Recipe {
    private String id;
    private String name;
    private String description;
    private String duration;
    private String ingredients;
    private String instructions;
    private String imageUrl;

    public Recipe() {
        // Required empty constructor for Firestore
    }

    public Recipe(String name, String description, String duration, String ingredients, String instructions) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
