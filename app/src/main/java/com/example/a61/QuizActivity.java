package com.example.a61;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class QuizActivity extends Activity {
    private List<String> userAnswers = new ArrayList<>();
    private List<String> correctAnswers = new ArrayList<>();
    private TextView questionTextView;
    private RadioGroup answersRadioGroup;
    private Button nextQuestionButton;
    private int currentQuestionIndex = 0;
    private List<JSONObject> questions = new ArrayList<>();
    private List<String> questionStrs = new ArrayList<>();
    private String topic;


    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> incorrectAnswersCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        answersRadioGroup = findViewById(R.id.answersRadioGroup);
        nextQuestionButton = findViewById(R.id.nextQuestionButton);

        topic = getIntent().getStringExtra("topic"); // Get the passed topic

        fetchQuestions();

        nextQuestionButton.setOnClickListener(v -> {
            if (answersRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }
            checkAnswer();
        });
        //mongo
//        mongoClient = new MongoClient(new MongoClientURI("mongodb://<nikitapestov>:<abcd>@<localhost>:<8080>/<incorectansw>"));
//        database = mongoClient.getDatabase("<incorectansw>");
//        incorrectAnswersCollection = database.getCollection("incorrect_answers");
    }

    private void checkAnswer() {
        int selectedId = answersRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedAnswer = selectedRadioButton.getText().toString();
        boolean isCorrect = selectedAnswer.equals(correctAnswers.get(currentQuestionIndex));
        setupResultAnimation(isCorrect);

        userAnswers.add(selectedAnswer);


        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isCorrect){
            int previousCorrectAnswers = sharedPref.getInt("CORRECT_ANS", 0);
            int updatedCorrectAnswers = previousCorrectAnswers + 1;
            editor.putInt("CORRECT_ANS", updatedCorrectAnswers);
        }
        else{
            int previousIncorrectAnswers = sharedPref.getInt("INCORECT_ANS", 0);
            int updatedIncorrectAnswers = previousIncorrectAnswers + 1;
            editor.putInt("INCORECT_ANS", updatedIncorrectAnswers);
        }
        editor.apply();  // Saves the data asynchronously


        if (!isCorrect) {
            saveIncorrectAnswerToMongoDB(questionTextView.getText().toString(), selectedAnswer, correctAnswers.get(currentQuestionIndex));
        }
    }
    private List<String> incorrectAnswersList = new ArrayList<>();

    private void saveIncorrectAnswerToMongoDB(String question, String userAnswer, String correctAnswer) {
        // Prepare the incorrect answer text
        String questionText = questionTextView.getText().toString();
        String incorrectAnswer = userAnswer + " (Correct Answer: " + correctAnswer + ")";
        String incorrectAnswerText = questionText + ": " + incorrectAnswer;

        // Add the incorrect answer to the list
        incorrectAnswersList.add(incorrectAnswerText);
        Log.d("QuizActivity", "Size of incorrectAnswersList: " + incorrectAnswersList.size());

        // Save the list to SharedPreferences as a set
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> incorrectAnswersSet = new HashSet<>(incorrectAnswersList);
        editor.putStringSet("INCORRECT_ANSWERS_SET", incorrectAnswersSet);
        editor.apply();
    }






    private static final Map<String, String> entities;
    static {
        entities = new HashMap<>();
        // Basic HTML entities
        entities.put("&quot;", "\""); // Quotation mark
        entities.put("&apos;", "'");  // Apostrophe
        entities.put("&amp;", "&");   // Ampersand
        entities.put("&lt;", "<");    // Less-than
        entities.put("&gt;", ">");    // Greater-than

        // Extended entities
        entities.put("&nbsp;", " ");  // Non-breaking space
        entities.put("&ndash;", "–"); // En dash
        entities.put("&mdash;", "—"); // Em dash
        entities.put("&iexcl;", "¡"); // Inverted exclamation mark
        entities.put("&cent;", "¢");  // Cent sign
        entities.put("&pound;", "£"); // Pound sign
        entities.put("&curren;", "¤"); // Currency sign
        entities.put("&yen;", "¥");   // Yen sign
        entities.put("&brvbar;", "|"); // Broken bar
        entities.put("&sect;", "§");  // Section sign
        entities.put("&uml;", "¨");   // Diaeresis
        entities.put("&copy;", "©");  // Copyright
        entities.put("&ordf;", "ª");  // Feminine ordinal indicator
        entities.put("&laquo;", "«"); // Left-pointing double angle quotation mark
        entities.put("&not;", "¬");   // Not sign
        entities.put("&reg;", "®");   // Registered sign
        entities.put("&macr;", "¯");  // Macron
        entities.put("&deg;", "°");   // Degree
        entities.put("&plusmn;", "±"); // Plus-minus
        entities.put("&sup2;", "²");  // Superscript two
        entities.put("&sup3;", "³");  // Superscript three
        entities.put("&acute;", "´"); // Acute accent
        entities.put("&micro;", "µ"); // Micro sign
        entities.put("&para;", "¶");  // Pilcrow sign
        entities.put("&middot;", "·"); // Middle dot
        entities.put("&cedil;", "¸"); // Cedilla
        entities.put("&sup1;", "¹");  // Superscript one
        entities.put("&ordm;", "º");  // Masculine ordinal indicator
        entities.put("&raquo;", "»"); // Right-pointing double angle quotation mark
        entities.put("&frac14;", "¼"); // Fraction one quarter
        entities.put("&frac12;", "½"); // Fraction one half
        entities.put("&frac34;", "¾"); // Fraction three quarters
        entities.put("&iquest;", "¿"); // Inverted question mark
        // Add more entities as needed
    }

    public static String decodeHtmlEntities(String source) {
        if (source == null) {
            return null;
        }
        for (Map.Entry<String, String> entry : entities.entrySet()) {
            source = source.replace(entry.getKey(), entry.getValue());
        }
        return source;
    }

    private void displayQuestion(JSONObject question) {
        try {
            String questionText = decodeHtmlEntities(question.getString("question"));
            JSONArray incorrectAnswers = question.getJSONArray("incorrect_answers");
            String correctAnswer = question.getString("correct_answer");

            questionTextView.setText(questionText);
            correctAnswers.add(correctAnswer);
            answersRadioGroup.removeAllViews();

            questionStrs.add(questionText);

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

    private void onAnswerSelected(View view) {
        nextQuestionButton.setEnabled(true);

    }


    private void showResults() {
        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
        intent.putStringArrayListExtra("userAnswers", new ArrayList<>(userAnswers));
        intent.putStringArrayListExtra("correctAnswers", new ArrayList<>(correctAnswers));
        intent.putStringArrayListExtra("questions", new ArrayList<>(questionStrs));
        startActivity(intent);
    }


    private void fetchQuestions() {
        int categoryId = getIntent().getIntExtra("topic_id", 0); // Ensure this is the correct default ID
        String url = "https://opentdb.com/api.php?amount=3&type=multiple&category=" + categoryId;

        Log.d("QuizActivity", "Fetching questions for category ID: " + categoryId);
        Log.d("QuizActivity", "URL: " + url); // Log the constructed URL

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("QuizActivity", "Failed to fetch questions", e);
                runOnUiThread(() -> Toast.makeText(QuizActivity.this, "Failed to fetch questions: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final String responseData = response.body().string();
                Log.d("QuizActivity", "Response data: " + responseData); // Log the API response
                runOnUiThread(() -> handleResponse(responseData));
            }
        });
    }



    private void handleResponse(String jsonData) {
        try {
            JSONObject jsonResponse = new JSONObject(jsonData);
            JSONArray jsonQuestions = jsonResponse.getJSONArray("results");

            for (int i = 0; i < jsonQuestions.length(); i++) {
                questions.add(jsonQuestions.getJSONObject(i));
            }

            if (!questions.isEmpty()) {
                runOnUiThread(() -> displayQuestion(questions.get(currentQuestionIndex)));
            } else {
                runOnUiThread(() -> Toast.makeText(QuizActivity.this, "No questions found.", Toast.LENGTH_SHORT).show());
            }
        } catch (JSONException e) {
            Log.e("QuizActivity", "JSON parsing error", e);
            runOnUiThread(() -> Toast.makeText(QuizActivity.this, "Error parsing questions: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
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

//                    nextQuestionButton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else {
            showResults();
        }
    }

    private void setupResultAnimation(boolean isCorrect) {
        int colorFrom = isCorrect ? Color.GREEN : Color.RED;
        int colorTo = Color.TRANSPARENT;
        ObjectAnimator animator = ObjectAnimator.ofArgb(answersRadioGroup, "backgroundColor", colorFrom, colorTo);
        animator.setDuration(500);
        animator.addUpdateListener(animation -> {
            // You can add a listener if you want to perform any action midway through the animation
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion(questions.get(currentQuestionIndex));
                } else {
                    showResults();
                }
            }
        });
        animator.start();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Close MongoDB client on activity destroy
//        if (mongoClient != null) {
//            mongoClient.close();
//        }
//    }




}
