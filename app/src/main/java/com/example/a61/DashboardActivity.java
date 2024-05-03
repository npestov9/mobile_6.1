package com.example.a61;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends Activity {
    private TextView welcomeTextView;
    private TextView taskCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        taskCountTextView = findViewById(R.id.taskCountTextView);

        String userName = "Your Name"; // This should be dynamically fetched based on user session
        welcomeTextView.setText("Hello, " + userName);
        taskCountTextView.setText("You have 2 tasks due");

        // Setup the click listener for the tasks using the method defined below
        findViewById(R.id.task1CardView).setOnClickListener(this::onTaskClick);
        findViewById(R.id.task2CardView).setOnClickListener(this::onTaskClick);
    }

    // Method to handle clicks on task cards
    public void onTaskClick(View view) {
        // Launch QuizActivity when a task card is clicked
        Intent intent = new Intent(DashboardActivity.this, QuizActivity.class);
        startActivity(intent);
    }
}

