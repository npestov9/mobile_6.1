package com.example.a61;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class IncorrectAnswersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incorrect_answers);

        // Get the ListView from the layout
        ListView incorrectAnswersListView = findViewById(R.id.incorrectAnswersListView);

        // Retrieve the list of incorrect answers from the intent
        ArrayList<String> incorrectAnswersList = getIntent().getStringArrayListExtra("incorrectAnswersList");

        // Create an ArrayAdapter to display the incorrect answers in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, incorrectAnswersList);
        incorrectAnswersListView.setAdapter(adapter);
    }
}
