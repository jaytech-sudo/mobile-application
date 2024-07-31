package com.example.myapplication;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FoodDonationFormActivity extends AppCompatActivity {

    private DatabaseReference foodListingsRef;
    private EditText foodTypeEditText, quantityEditText, specialInstructionsEditText;
    private TimePicker availableUntilTimePicker;
    private Button submitButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_donation_form);

        // Initialize Firebase database reference
        foodListingsRef = FirebaseDatabase.getInstance().getReference("foodListings");

        // Initialize UI components
        foodTypeEditText = findViewById(R.id.foodTypeEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        availableUntilTimePicker = findViewById(R.id.availableUntilTimePicker);
        specialInstructionsEditText = findViewById(R.id.specialInstructionsEditText);
        submitButton = findViewById(R.id.submitButton);

        if (submitButton == null) {
            Log.e("FoodDonationFormActivity", "Submit button not found");
            return;  // Exit the method to avoid further crashes
        }

        // Set click listener for submit button
        submitButton.setOnClickListener(view -> {
            String foodType = foodTypeEditText.getText().toString().trim();
            String quantityString = quantityEditText.getText().toString().trim();
            String specialInstructions = specialInstructionsEditText.getText().toString().trim();

            if (validateInput(foodTypeEditText, quantityEditText, foodType, quantityString)) {
                int quantity = Integer.parseInt(quantityString);
                int hour = availableUntilTimePicker.getHour();
                int minute = availableUntilTimePicker.getMinute();
                String availableUntil = hour + ":" + minute;
                addFoodListing(foodType, quantity, availableUntil, specialInstructions);
            }
        });
    }

    private boolean validateInput(EditText foodTypeEditText, EditText quantityEditText, String foodType, String quantityString) {
        if (TextUtils.isEmpty(foodType)) {
            foodTypeEditText.setError("Please enter the food type");
            return false;
        } else {
            foodTypeEditText.setError(null);
        }

        if (TextUtils.isEmpty(quantityString)) {
            quantityEditText.setError("Please enter the quantity");
            return false;
        } else {
            quantityEditText.setError(null);
        }

        try {
            Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            quantityEditText.setError("Please enter a valid quantity");
            return false;
        }

        return true;
    }

    private void addFoodListing(String foodType, int quantity, String availableUntil, String specialInstructions) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Generate a unique foodListingId using push()
            DatabaseReference newListingRef = foodListingsRef.push();
            String listingId = newListingRef.getKey();

            // Create FoodListings object with all parameters, including listingId
            FoodListings newListing = new FoodListings(listingId, foodType, quantity, availableUntil, specialInstructions, userId);

            // Upload to Firebase using the newListingRef
            newListingRef.setValue(newListing)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(FoodDonationFormActivity.this, "Food listing added", Toast.LENGTH_SHORT).show();
                            // ... (clear input fields if needed)
                        } else {
                            Toast.makeText(FoodDonationFormActivity.this, "Failed to add food listing", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
