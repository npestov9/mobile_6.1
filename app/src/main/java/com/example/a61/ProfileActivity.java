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
    private TextView usernameTextView, emailTextView, corectAnsTextView, incorectAnsTextView, totalQsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        corectAnsTextView = findViewById(R.id.correctlyAnsweredTextView);
        incorectAnsTextView = findViewById(R.id.incorrectAnswersTextView);
        totalQsTextView = findViewById(R.id.totalQuestionsTextView);

        // Retrieve the username and email from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String username = sharedPref.getString("USERNAME", "Default User"); // Provide a default value
        String email = sharedPref.getString("EMAIL", "No email set");

        Integer correctAns = sharedPref.getInt("CORRECT_ANS", 0);
        Integer incorrectAns = sharedPref.getInt("INCORECT_ANS", 0);


        // Set the retrieved data to TextViews
        totalQsTextView.setText("Total Questions: " + String.valueOf(correctAns + incorrectAns));
        corectAnsTextView.setText("Correctly Answered: " +String.valueOf(correctAns));
        incorectAnsTextView.setText("Incorrectly Answered: " +String.valueOf(incorrectAns));
        usernameTextView.setText(username);
        emailTextView.setText(email);
    }
}

