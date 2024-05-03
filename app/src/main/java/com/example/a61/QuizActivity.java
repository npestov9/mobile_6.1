package com.example.a61;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuizActivity extends Activity {
    private List<String> userAnswers = new ArrayList<>();
    private List<String> correctAnswers = new ArrayList<>();
    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private Button nextQuestionButton;
    private int currentQuestionIndex = 0;
    private List<JSONObject> questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        nextQuestionButton = findViewById(R.id.nextQuestionButton);

        fetchQuestions();  // Assume this method fetches the questions and populates `questions`

        nextQuestionButton.setOnClickListener(v -> {
            int selectedId = answersRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedId);
            userAnswers.add(selectedRadioButton.getText().toString());
            checkAnswer(selectedRadioButton.getText().toString());

            currentQuestionIndex++;  // Move this line before displayQuestion
            if (currentQuestionIndex < questions.size()) {
                displayQuestion(questions.get(currentQuestionIndex));
            } else {
                showResults();
            }
        });

    }

    private void checkAnswer(String selectedAnswer) {
        String correctAnswer = correctAnswers.get(currentQuestionIndex);
        if (selectedAnswer.equals(correctAnswer)) {
            // This answer is correct
            // Optionally handle correct answer
        } else {
            // This answer is incorrect
            // Optionally handle incorrect answer
        }

        nextQuestionButton.setVisibility(View.VISIBLE);
    }

    public static String decodeHtmlEntities(String source) {
        return StringEscapeUtils.unescapeHtml4(source);
    }

    private void displayQuestion(JSONObject question) {
        try {
            String questionText = decodeHtmlEntities(question.getString("question"));
            JSONArray incorrectAnswers = question.getJSONArray("incorrect_answers");
            String correctAnswer = decodeHtmlEntities(question.getString("correct_answer"));

            questionTextView.setText(questionText);
            correctAnswers.add(correctAnswer);
            answersRadioGroup.removeAllViews();

            for (int i = 0; i < incorrectAnswers.length(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(decodeHtmlEntities(incorrectAnswers.getString(i)));
                answersRadioGroup.addView(radioButton);
            }

            RadioButton correctRadioButton = new RadioButton(this);
            correctRadioButton.setText(correctAnswer);
            answersRadioGroup.addView(correctRadioButton);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showResults() {
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putStringArrayListExtra("userAnswers", new ArrayList<>(userAnswers));
        intent.putStringArrayListExtra("correctAnswers", new ArrayList<>(correctAnswers));
        startActivity(intent);
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
            JSONObject currentQuestion = questions.get(currentQuestionIndex);
            runOnUiThread(() -> {
                try {
                    String question = currentQuestion.getString("question");
                    JSONArray incorrectAnswers = currentQuestion.getJSONArray("incorrect_answers");
                    String correctAnswer = currentQuestion.getString("correct_answer");

                    questionTextView.setText(question);
                    correctAnswers.add(correctAnswer);

                    answersRadioGroup.removeAllViews();
                    for (int i = 0; i < incorrectAnswers.length(); i++) {
                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setText(incorrectAnswers.getString(i));
                        answersRadioGroup.addView(radioButton);
                    }

                    RadioButton correctRadioButton = new RadioButton(this);
                    correctRadioButton.setText(correctAnswer);
                    answersRadioGroup.addView(correctRadioButton);

                    nextQuestionButton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else {
            showResults();
        }
    }




}
