package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class NgoManagerDashboardActivity extends AppCompatActivity implements FoodListingAdapter.OnItemClickListener, FoodListingAdapter.OnRequestClickListener {

    private List<FoodListings> foodListings;
    private FoodListingAdapter adapter;

    @Override
    public void onRequestClick(FoodListings foodListing, int quantity) {
        // Handle the request click here
        // You'll likely need to send a request to Firebase or perform some other action
        String foodListingId = foodListing.getId();
        String ngoId = getCurrentNgoId();

        Log.d("Request", "Food Listing ID: " + foodListingId);
        Log.d("Request", "NGO ID: " + ngoId);

        // Create FoodRequest object
        FoodRequest request = new FoodRequest(foodListingId, ngoId, quantity, "Pending", System.currentTimeMillis());

        // Send request to Firebase
        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
        requestsRef.push().setValue(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NgoManagerDashboardActivity.this, "Request sent!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NgoManagerDashboardActivity.this, "Failed to send request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_manager_dashboard);

        foodListings = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodListingAdapter(foodListings, this, this, this);
        recyclerView.setAdapter(adapter);

        loadFoodListingsFromRealtimeDatabase();
    }

    private void loadFoodListingsFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference foodListingsRef = database.getReference("foodListings");

        foodListingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodListings.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    FoodListings foodListing = childSnapshot.getValue(FoodListings.class);
                    if (foodListing != null) {
                        foodListings.add(foodListing);
                        Log.d("NgoDashboard", "Food Listing: " + foodListing.getFoodType());
                    }
                }
                adapter.notifyDataSetChanged();
                Log.d("NgoDashboard", "Number of food listings fetched: " + foodListings.size());
                for (FoodListings listing : foodListings) {
                    Log.d("NgoDashboard", "Food Listing: " + listing.getFoodType());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NgoDashboard", "Error fetching food listings", error.toException());
            }
        });
    }

    @Override
    public void onItemClick(FoodListings foodListing) {
        // Handle item click here
        String foodListingId = foodListing.getId();
        Intent intent = new Intent(NgoManagerDashboardActivity.this, FoodListingDetailsActivity.class);
        intent.putExtra("FOOD_LISTING_ID", foodListingId);
        startActivity(intent);
    }

    // Method to get the current NGO's ID
    private String getCurrentNgoId() {
        // Replace this with your actual logic to fetch the NGO ID
        String ngoId = "actualNgoId"; // Replace with actual ID retrieval
        return ngoId;
    }
}
