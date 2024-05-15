package com.example.a61;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
        Integer totalQs = correctAns + incorrectAns;

        // Set the retrieved data to TextViews
        totalQsTextView.setText(String.valueOf(totalQs));
        corectAnsTextView.setText(String.valueOf(correctAns));
        incorectAnsTextView.setText(String.valueOf(incorrectAns));
        usernameTextView.setText(username);
        emailTextView.setText(email);


        Button shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the message to share (e.g., statistics)
                String message =
                        "Check " + username + "'s quiz stats" + "\n\n"+
                        "Total Questions: " + (totalQs) + "\n" +
                        "Correctly Answered: " + correctAns + "\n" +
                        "Incorrect Answers: " + incorrectAns;

                // Create an Intent to share the message
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);

                // Start the activity for sharing
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        Button upgradeBtn = findViewById(R.id.upgradeButton);
        upgradeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpgradeActivity.class);
                startActivity(intent);
            }
        });
    }
}

