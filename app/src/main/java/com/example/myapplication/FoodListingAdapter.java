package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.util.Log;
public class FoodListingAdapter extends RecyclerView.Adapter<FoodListingAdapter.FoodListingViewHolder> {

    private List<FoodListings> foodListings;
    private OnItemClickListener listener;
    private OnRequestClickListener requestListener;
    private Context context;

    // Define the OnItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(FoodListings foodListing);
    }

    // Define the OnRequestClickListener interface
    public interface OnRequestClickListener {
        void onRequestClick(FoodListings foodListing, int quantity);
    }

    // Constructor
    public FoodListingAdapter(List<FoodListings> foodListings, OnItemClickListener listener,
                              OnRequestClickListener requestListener, Context context) {
        this.foodListings = foodListings;
        this.listener = listener;
        this.requestListener = requestListener;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_food_listing, parent, false);
        return new FoodListingViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FoodListingViewHolder holder, int position) {
        FoodListings foodListing = foodListings.get(position);

        // Correct way to set the food type:
        if (foodListing.getFoodType() != null) {
            Log.d("FoodListingAdapter", "Food Type: " + foodListing.getFoodType());
            holder.foodNameTextView.setText("foodType: " + foodListing.getFoodType()); // Fixed typo
        } else {holder.foodNameTextView.setText("Food Type Not Available"); // Or handle null as needed
        }
        holder.foodNameTextView.setText("foodType: " + foodListing.getFoodType()); // Capital'F' in getFoodType()
        holder.quantityTextView.setText("Quantity: " + foodListing.getQuantity());
        holder.specialInstructionsTextView.setText("Instructions: " + foodListing.getDescription());
        holder.availableTimeTextView.setText("Available Until: " + foodListing.getAvailableUntil());

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(foodListings.get(pos));
            }
        });

        // Set click listener for the request button
        holder.requestButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && requestListener != null) {
                // You might need to get the quantity from an EditText or other input field
                int quantity = 1; // Replace with actual quantity
                requestListener.onRequestClick(foodListings.get(pos), quantity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodListings.size();
    }

    static class FoodListingViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView quantityTextView;
        TextView specialInstructionsTextView;
        TextView availableTimeTextView;
        Button requestButton;

        public FoodListingViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            specialInstructionsTextView = itemView.findViewById(R.id.specialInstructionsTextView);
            availableTimeTextView = itemView.findViewById(R.id.availableTimeTextView);
            requestButton = itemView.findViewById(R.id.requestButton);
        }
    }
}
