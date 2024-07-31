package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        CardView cardRestaurantManager = findViewById(R.id.cardRestaurantManager);
        CardView cardNgoManager = findViewById(R.id.cardNgoManager);
        CardView cardAdmin = findViewById(R.id.cardAdmin);

        cardRestaurantManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Restaurant Manager clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ManagerDashboardActivity.class);
                startActivity(intent);
            }
        });

        cardNgoManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "NGO Manager clicked", Toast.LENGTH_SHORT).show();
                // Start NgoManagerDashboardActivity (Corrected Intent)
                Intent intent = new Intent(MainActivity.this, NgoManagerDashboardActivity.class);
                startActivity(intent);
            }
        });

        cardAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Admin clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}