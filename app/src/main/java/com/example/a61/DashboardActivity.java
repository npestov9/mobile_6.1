package com.example.a61;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends Activity {
    private TextView welcomeTextView;
    private TextView taskCountTextView;
    private Map<String, Integer> topicsMap = new HashMap<>();
    private List<String> selectedTopics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        taskCountTextView = findViewById(R.id.taskCountTextView);

        // Initialize selectedTopics list
        selectedTopics = new ArrayList<>();

        Intent intent = getIntent();
        ArrayList<String> topics = intent.getStringArrayListExtra("selectedTopics");
        if (topics != null) {
            selectedTopics.addAll(topics);
        }

        HashMap<String, Integer> receivedMap = (HashMap<String, Integer>) intent.getSerializableExtra("topicsMap");
        if (receivedMap != null) {
            topicsMap.putAll(receivedMap);
        }

        welcomeTextView.setText("Hello, Your Name");
        updateTaskCards();
    }


    public void updateTaskCards() {
        LinearLayout tasksContainer = findViewById(R.id.tasksContainer);
        tasksContainer.removeAllViews();  // Clear previous task views

        for (String topic : selectedTopics) {
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 20);
            cardView.setLayoutParams(layoutParams);
            cardView.setCardBackgroundColor(Color.WHITE);
            cardView.setRadius(8);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT,
                    CardView.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(topic + " - Click to start quiz");
            textView.setTextSize(18);
            textView.setPadding(16, 16, 16, 16);
            textView.setTextColor(Color.BLACK);

            cardView.addView(textView);
            cardView.setOnClickListener(v -> onTaskClick(topic));

            tasksContainer.addView(cardView);
        }
    }

    public void onTaskClick(String topic) {
        Intent intent = new Intent(DashboardActivity.this, QuizActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }

//    public void onTaskClick(String topic) {
//        int categoryId = topicsMap.get(topic);
//        Intent intent = new Intent(DashboardActivity.this, QuizActivity.class);
//        intent.putExtra("category_id", categoryId); // Pass category ID to QuizActivity
//        startActivityForResult(intent, categoryId); // Use category ID as request code for simplicity
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // Find the topic name using category ID (requestCode)
            String completedTopic = null;
            for (Map.Entry<String, Integer> entry : topicsMap.entrySet()) {
                if (entry.getValue().equals(requestCode)) {
                    completedTopic = entry.getKey();
                    break;
                }
            }

            if (completedTopic != null) {
                selectedTopics.remove(completedTopic);
                updateTaskCards(); // Refresh task cards to remove the completed one
            }
        }
    }
}
