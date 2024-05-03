package com.example.a61;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

public class ResultsActivity extends Activity {
    private TextView resultsTextView;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsTextView = findViewById(R.id.resultsTextView);
        continueButton = findViewById(R.id.continueButton);

        resultsTextView.setText("Quiz Completed. Display your results here.");

        continueButton.setOnClickListener(v -> finish());
    }
}
