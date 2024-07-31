package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<FoodRequest> requests;
    private OnApproveClickListener approveClickListener;

    public RequestAdapter(List<FoodRequest> requests, OnApproveClickListener approveClickListener) {
        this.requests = requests;
        this.approveClickListener = approveClickListener;
    }

    public interface OnApproveClickListener {
        void onApproveClick(FoodRequest request);
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        FoodRequest request = requests.get(position);
        holder.foodListingIdTextView.setText("Food Listing ID: " + request.getFoodListingId());
        holder.ngoIdTextView.setText("NGO ID: " + request.getNgoId());
        holder.quantityTextView.setText("Quantity: " + request.getQuantityRequested());
        holder.statusTextView.setText("Status: " + request.getRequestStatus());

        // Set click listener for the approve button
        holder.btnApprove.setOnClickListener(v -> {
            if (approveClickListener != null) {
                approveClickListener.onApproveClick(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView foodListingIdTextView;
        TextView ngoIdTextView;
        TextView quantityTextView;
        TextView statusTextView;
        Button btnApprove; // Add the approve button

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            foodListingIdTextView = itemView.findViewById(R.id.foodListingIdTextView);
            ngoIdTextView = itemView.findViewById(R.id.ngoIdTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            btnApprove = itemView.findViewById(R.id.btnApprove); // Initialize the button
        }
    }
}
