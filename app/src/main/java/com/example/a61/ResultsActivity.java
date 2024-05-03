package com.example.a61;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class ResultsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        List<String> userAnswers = getIntent().getStringArrayListExtra("userAnswers");
        List<String> correctAnswers = getIntent().getStringArrayListExtra("correctAnswers");

        LinearLayout resultsLayout = findViewById(R.id.resultsLayout);  // Assuming this is your layout to add results views

        for (int i = 0; i < userAnswers.size(); i++) {
            TextView resultView = new TextView(this);
            resultView.setText("Q" + (i + 1) + ": " + userAnswers.get(i));
            resultView.setTextSize(16f);

            if (userAnswers.get(i).equals(correctAnswers.get(i))) {
                resultView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                resultView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            resultsLayout.addView(resultView);
        }
    }
}
