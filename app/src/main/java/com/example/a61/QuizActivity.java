package com.example.a61;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Button;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class QuizActivity extends Activity {
    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private Button submitAnswerButton, nextQuestionButton;
    private ArrayList<JSONObject> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);
        nextQuestionButton = findViewById(R.id.nextQuestionButton);

        submitAnswerButton.setOnClickListener(v -> checkAnswer());
        nextQuestionButton.setOnClickListener(v -> showNextQuestion());

        fetchQuestions();
    }

    private void fetchQuestions() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://opentdb.com/api.php?amount=3&type=multiple";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray jsonQuestions = jsonResponse.getJSONArray("results");
                    for (int i = 0; i < jsonQuestions.length(); i++) {
                        questions.add(jsonQuestions.getJSONObject(i));
                    }
                    runOnUiThread(() -> showNextQuestion());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            try {
                JSONObject currentQuestion = questions.get(currentQuestionIndex);
                String question = currentQuestion.getString("question");
                questionTextView.setText(question);
                JSONArray incorrectAnswers = currentQuestion.getJSONArray("incorrect_answers");
                String correctAnswer = currentQuestion.getString("correct_answer");

                answersRadioGroup.removeAllViews();
                for (int i = 0; i < incorrectAnswers.length(); i++) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(incorrectAnswers.getString(i));
                    answersRadioGroup.addView(radioButton);
                }

                // Add correct answer
                RadioButton correctRadioButton = new RadioButton(this);
                correctRadioButton.setText(correctAnswer);
                answersRadioGroup.addView(correctRadioButton);

                submitAnswerButton.setVisibility(View.VISIBLE);
                nextQuestionButton.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Finish or show results
            showResults();
        }
    }

    private void checkAnswer() {
        // Implement answer checking logic here
        nextQuestionButton.setVisibility(View.VISIBLE);
        submitAnswerButton.setVisibility(View.GONE);
        currentQuestionIndex++;
    }

    private void showResults() {
        // Intent to a new Activity that shows results
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);
    }
}
