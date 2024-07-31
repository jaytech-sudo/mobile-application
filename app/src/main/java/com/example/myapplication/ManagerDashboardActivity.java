package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ManagerDashboardActivity extends AppCompatActivity implements FoodListingAdapter.OnItemClickListener, FoodListingAdapter.OnRequestClickListener, RequestAdapter.OnApproveClickListener {

    private List<FoodListings> foodListings;
    private FoodListingAdapter adapter;
    private List<FoodRequest> requests;
    private RequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        foodListings = new ArrayList<>();
        requests = new ArrayList<>();

        // Food Listings RecyclerView setup
        RecyclerView foodListingsRecyclerView = findViewById(R.id.rvFoodListings);
        if (foodListingsRecyclerView != null) {
            foodListingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new FoodListingAdapter(foodListings, this, this, this);
            foodListingsRecyclerView.setAdapter(adapter);
        } else {
            Log.e("ManagerDashboardActivity", "Food Listings RecyclerView not found!");
        }

        // Requests RecyclerView setup
        RecyclerView requestsRecyclerView = findViewById(R.id.requestsRecyclerView);
        if (requestsRecyclerView != null) {
            requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            requestAdapter = new RequestAdapter(requests, this);
            requestsRecyclerView.setAdapter(requestAdapter);
        } else {
            Log.e("ManagerDashboardActivity", "Requests RecyclerView not found!");
        }

        // Initialize the "Add New Listing" button
        Button btnAddNewListing = findViewById(R.id.btnAddNewListing);
        if (btnAddNewListing != null) {
            btnAddNewListing.setOnClickListener(v -> {
                Log.d("AddNewListing", "Button clicked!");
                // Navigate to Add New Listing Activity
                Intent intent = new Intent(ManagerDashboardActivity.this, FoodDonationFormActivity.class);
                startActivity(intent);
            });
        } else {
            Log.e("ManagerDashboardActivity", "Add New Listing Button not found!");
        }

        loadFoodListingsFromRealtimeDatabase();
        loadRequestsFromFirebase();
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
                        Log.d("ManagerDashboard", "Food Listing: " + foodListing.getFoodType());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ManagerDashboard", "Error fetching food listings", error.toException());
            }
        });
    }

    private void loadRequestsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference requestsRef = database.getReference("requests");

        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requests.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    FoodRequest request = childSnapshot.getValue(FoodRequest.class);
                    if (request != null) {
                        requests.add(request);
                        Log.d("ManagerDashboard", "Request: " + request.getFoodListingId());
                    }
                }
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ManagerDashboard", "Error fetching requests", error.toException());
            }
        });
    }

    @Override
    public void onItemClick(FoodListings foodListing) {
        // Handle item click here. For example, navigate to the details page
        Intent intent = new Intent(this, FoodListingDetailsActivity.class);
        intent.putExtra("FOOD_LISTING_ID", foodListing.getId());
        startActivity(intent);
    }

    @Override
    public void onRequestClick(FoodListings foodListing, int quantity) {
        // Handle the request click here. For example, send a request to Firebase:
        String foodListingId = foodListing.getId();
        String ngoId = getCurrentNgoId();

        Log.d("Request", "Food Listing ID: " + foodListingId);
        Log.d("Request", "NGO ID: " + ngoId);

        // Create FoodRequest object
        FoodRequest request = new FoodRequest(foodListingId, ngoId, quantity, "Pending", System.currentTimeMillis());
        // Send request to Firebase
        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("requests");
        requestsRef.push().setValue(request)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(ManagerDashboardActivity.this, "Request sent!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(ManagerDashboardActivity.this, "Failed to send request: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onApproveClick(FoodRequest request) {
        // Handle the approval logic here.
        // Update the request status in Firebase to "Approved".
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("requests")
                .child(request.getFoodListingId()); // Assuming foodListingId is unique

        requestRef.child("requestStatus").setValue("Approved")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request Approved!", Toast.LENGTH_SHORT).show();
                    // Refresh the RecyclerView to reflect the change
                    loadRequestsFromFirebase();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to approve request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getCurrentNgoId() {
        // Implement this method to return the current NGO's ID
        return "sampleNgoId"; // Replace with actual implementation
    }
}
