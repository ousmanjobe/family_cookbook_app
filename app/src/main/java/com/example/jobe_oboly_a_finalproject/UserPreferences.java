package com.example.jobe_oboly_a_finalproject;

public class UserPreferences {
    private boolean isDarkTheme;

    public UserPreferences(boolean isDarkTheme) {
        this.isDarkTheme = isDarkTheme;
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }
}
