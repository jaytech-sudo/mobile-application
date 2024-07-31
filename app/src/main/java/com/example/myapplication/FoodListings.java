package com.example.myapplication;

import android.util.Log;

public class FoodListings {
    private String id;
    private String foodType; // Changed from 'name' to 'foodType'
    private String description;
    private int quantity;
    private String availableUntil;
    private String userId;

    // No-argument constructor for Firebase
    public FoodListings() {
        this.id = ""; // Initialize id
        this.foodType = "";
        this.description = "";
        this.quantity = 0;
        this.availableUntil = "";
        this.userId = "";
    }

    public FoodListings(String id, String foodType, int quantity, String availableUntil, String specialInstructions, String userId) {
        this.id = id;
        this.foodType = foodType; // Changed from 'name' to 'foodType'
        this.description = specialInstructions;
        this.quantity = quantity;
        this.availableUntil = availableUntil;
        this.userId = userId;

        Log.d("FoodListings", "ID set to: " + this.id);
        Log.d("FoodListings", "Name set to: " + this.foodType);
        Log.d("FoodListings", "Description set to: " + this.description);
        Log.d("FoodListings", "Quantity set to: " + this.quantity);
        Log.d("FoodListings", "Available Until set to: " + this.availableUntil);
        Log.d("FoodListings", "User ID set to: " + this.userId);
    }

    // Getters
    public String getId() { // Add this getter
        return id;
    }

    public String getFoodType() {return foodType;}

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAvailableUntil() {
        return availableUntil;
    }

    public String getUserId() {
        return userId;
    }

    // Setters (if needed)
    // ...
}
