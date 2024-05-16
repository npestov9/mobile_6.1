package com.example.a61;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IncorrectAnswersActivity extends AppCompatActivity {

    private List<String> incorrectAnswersList = new ArrayList<>(); // Initialize the list
    private ListView incorrectAnswersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incorrect_answers);

        // Retrieve the list from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        Set<String> incorrectAnswersSet = sharedPref.getStringSet("INCORRECT_ANSWERS_SET", new HashSet<>());
        incorrectAnswersList.addAll(incorrectAnswersSet);

        // Split the string and add each line to the list
        List<String> formattedIncorrectAnswers = new ArrayList<>();
        for (String answer : incorrectAnswersList) {
            String[] lines = answer.split("\n");
            for (String line : lines) {
                formattedIncorrectAnswers.add(line);
            }
        }

        // Initialize the ListView
        incorrectAnswersListView = findViewById(R.id.incorrectAnswersListView);

        // Set up the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formattedIncorrectAnswers);
        incorrectAnswersListView.setAdapter(adapter);
    }
}
