package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodListingDetailsActivity extends AppCompatActivity {

    private TextView foodNameTextView;
    private TextView quantityTextView;
    private TextView specialInstructionsTextView;
    private TextView availableTimeTextView;

    private FirebaseDatabase database;
    private DatabaseReference foodListingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_listing_details);

        foodNameTextView = findViewById(R.id.foodNameValueTextView);
        quantityTextView = findViewById(R.id.quantityValueTextView);
        specialInstructionsTextView = findViewById(R.id.specialInstructionsValueTextView);
        availableTimeTextView = findViewById(R.id.availableTimeValueTextView);

        database = FirebaseDatabase.getInstance();
        foodListingsRef = database.getReference("foodListings");

        String foodListingId = getIntent().getStringExtra("FOOD_LISTING_ID");
        Log.d("FoodListingDetails", "Received foodListingId: " + foodListingId);

        if (foodListingId != null && !foodListingId.isEmpty()) {
            fetchFoodListingDetails(foodListingId);
        } else {
            Log.e("FoodListingDetails", "Invalid foodListingId received.");
            // Handle the case where no or invalid ID is received (e.g., display an error message)
        }
    }

    private void fetchFoodListingDetails(String foodListingId) {
        foodListingsRef.child(foodListingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FoodListings foodListing = snapshot.getValue(FoodListings.class);
                if (foodListing != null) {
                    Log.d("FoodListingDetails", "Food Type: " + foodListing.getFoodType()); // Use getFoodType()
                    Log.d("FoodListingDetails", "Quantity: " + foodListing.getQuantity());
                    Log.d("FoodListingDetails", "Special Instructions: " + foodListing.getDescription());
                    Log.d("FoodListingDetails", "Available Until: " + foodListing.getAvailableUntil());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            foodNameTextView.setText(foodListing.getFoodType());
                            quantityTextView.setText(String.valueOf(foodListing.getQuantity()));
                            specialInstructionsTextView.setText(foodListing.getDescription());
                            availableTimeTextView.setText(foodListing.getAvailableUntil());
                        }
                    });
                } else {
                    Log.e("FoodListingDetails", "Food listing not found for ID: " + foodListingId);
                    // Handle the case where the food listing is not found (e.g., display an error message)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FoodListingDetails", "Error fetching food listing details", error.toException());
                // Handle the database error (e.g., display an error message)
            }
        });
    }
}