package com.example.a61;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class ResultsActivity extends Activity {
    private LinearLayout questionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        questionsContainer = findViewById(R.id.questionsContainer);

        ArrayList<String> userAnswers = getIntent().getStringArrayListExtra("userAnswers");
        ArrayList<String> correctAnswers = getIntent().getStringArrayListExtra("correctAnswers");
        ArrayList<String> questions = getIntent().getStringArrayListExtra("questions");

        for (int i = 0; i < questions.size(); i++) {
            TextView questionView = new TextView(this);
            questionView.setText(questions.get(i));
            questionView.setTextSize(16f);
            questionsContainer.addView(questionView);

            TextView answerView = new TextView(this);
            answerView.setText("Your answer: " + userAnswers.get(i));
            answerView.setTextSize(16f);

            if (userAnswers.get(i).equals(correctAnswers.get(i))) {
                answerView.setTextColor(Color.GREEN);
            } else {
                answerView.setTextColor(Color.RED);
                TextView correctAnswerView = new TextView(this);
                correctAnswerView.setText("Correct answer: " + correctAnswers.get(i));
                correctAnswerView.setTextColor(Color.GREEN);
                correctAnswerView.setTextSize(16f);
                questionsContainer.addView(correctAnswerView);
            }
            questionsContainer.addView(answerView);
        }
    }
}
