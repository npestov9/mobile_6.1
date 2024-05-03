package com.example.a61;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class QuizActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);  // Ensure this layout exists

        // You can add quiz logic or a message here
        TextView textView = new TextView(this);
        textView.setText("Quiz Placeholder");
        textView.setTextSize(24f);
        setContentView(textView);
    }
}


