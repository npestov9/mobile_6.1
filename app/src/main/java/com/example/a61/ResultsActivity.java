package com.example.a61;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsActivity extends Activity {
    private ArrayList<String> userAnswers;
    private ArrayList<String> correctAnswers;
    private ArrayList<String> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Retrieve data passed from the QuizActivity
        Intent intent = getIntent();
        userAnswers = intent.getStringArrayListExtra("userAnswers");
        correctAnswers = intent.getStringArrayListExtra("correctAnswers");
        questions = intent.getStringArrayListExtra("questions");

        displayResults();

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming MainActivity is the landing page after results
                Intent mainIntent = new Intent(ResultsActivity.this, DashboardActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }

    private void displayResults() {
        LinearLayout resultsContainer = findViewById(R.id.resultsContainer);
        resultsContainer.removeAllViews();

        for (int i = 0; i < questions.size(); i++) {
            // Create a TextView for the question
            TextView questionView = new TextView(this);
            questionView.setText("Question " + (i + 1) + ": " + questions.get(i));
            questionView.setTextColor(Color.BLACK);
            questionView.setTextSize(18f);
            resultsContainer.addView(questionView);

            // Create a TextView for the user's answer
            TextView answerView = new TextView(this);
            answerView.setTextSize(16f);
            answerView.setPadding(0, 10, 0, 10);

            // Check if the answer was correct
            if (userAnswers.get(i).equals(correctAnswers.get(i))) {
                answerView.setTextColor(Color.GREEN);
                answerView.setText("Correct answer: " + userAnswers.get(i) + " ✅");
            } else {
                answerView.setTextColor(Color.RED);
                answerView.setText("Your answer: " + userAnswers.get(i) + " ❌\nCorrect answer: " + correctAnswers.get(i));
            }
            resultsContainer.addView(answerView);
        }
    }
}
