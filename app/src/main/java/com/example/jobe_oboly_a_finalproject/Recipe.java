package com.example.jobe_oboly_a_finalproject;

import java.io.Serializable;
import java.util.UUID;

public class Recipe implements Serializable {
    private String id;
    private String name;
    private String description;
    private String duration;
    private String ingredients;
    private String instructions;
    private String imageUrl;
    private boolean isFavorite;
    private boolean isVisited;
    private long visitedDate;
    private String userId;

    public Recipe() {
        // Required empty constructor for Room
    }

    public Recipe(String id, String name, String description, String duration, String ingredients, String instructions, String imageUrl, boolean isFavorite, boolean isVisited, long visitedDate, String userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
        this.isVisited = isVisited;
        this.visitedDate = visitedDate;
        this.userId = userId;
    }

    public static Recipe fromEntity(RecipeEntity entity) {
        return new Recipe(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDuration(),
                entity.getIngredients(),
                entity.getInstructions(),
                entity.getImageUrl(),
                entity.isFavorite(),
                entity.isVisited(),
                entity.getVisitedDate(),
                entity.getUserId()
        );
    }

    public RecipeEntity toEntity() {
        return new RecipeEntity(
                id != null ? id : UUID.randomUUID().toString(),
                name,
                description,
                duration,
                ingredients,
                instructions,
                imageUrl,
                isFavorite,
                isVisited,
                visitedDate,
                userId
        );
    }

    // Getters and setters...

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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public long getVisitedDate() {
        return visitedDate;
    }

    public void setVisitedDate(long visitedDate) {
        this.visitedDate = visitedDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
