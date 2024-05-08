package com.example.a61;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {
    private TextView usernameTextView, emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);

        // Retrieve the username and email from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String username = sharedPref.getString("USERNAME", "Default User"); // Provide a default value
        String email = sharedPref.getString("EMAIL", "No email set");

        // Set the retrieved data to TextViews
        usernameTextView.setText(username);
        emailTextView.setText(email);
    }
}

