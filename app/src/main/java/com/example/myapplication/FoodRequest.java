package com.example.myapplication;

public class FoodRequest {private String foodListingId;
    private String ngoId;
    private int quantityRequested;
    private String requestStatus;
    private long timestamp;

    // No-argument constructor for Firebase
    public FoodRequest() {
        // Default constructor required for Firebase
    }

    // Constructor with parameters
    public FoodRequest(String foodListingId, String ngoId, int quantityRequested, String requestStatus, long timestamp) {
        this.foodListingId = foodListingId;
        this.ngoId = ngoId;
        this.quantityRequested = quantityRequested;this.requestStatus = requestStatus;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getFoodListingId() {
        return foodListingId;
    }

    public String getNgoId() {
        return ngoId;
    }

    public int getQuantityRequested() {
        return quantityRequested;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    // ... (Add getters and setters for other properties)
}