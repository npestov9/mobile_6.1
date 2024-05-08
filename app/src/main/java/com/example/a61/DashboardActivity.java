package com.example.a61;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DashboardActivity extends Activity {
    private static final Map<Integer, String> topicsMap = new HashMap<>();
    private static final Set<Integer> completedTopics = new HashSet<>();

    private TextView welcomeTextView;
    private TextView taskCountTextView;
    private LinearLayout tasksContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        taskCountTextView = findViewById(R.id.taskCountTextView);
        tasksContainer = findViewById(R.id.tasksContainer);

        welcomeTextView.setText("Hello, and welcome");
        updateTaskCards();

        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Profile button clicked!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateTaskCards() {
        tasksContainer.removeAllViews();

        // Assuming topicsMap is updated with the selected topics, we iterate through it to display tasks
        for (Map.Entry<Integer, String> entry : topicsMap.entrySet()) {
            if (!completedTopics.contains(entry.getKey())) {
                addTopicCard(entry.getKey(), entry.getValue());
            }
        }

        int remainingTasks = topicsMap.size() - completedTopics.size();
        taskCountTextView.setText("You have " + remainingTasks + " tasks due");
    }

    static {
         topicsMap.put(10, "Entertainment: Books");
         topicsMap.put(11, "Entertainment: Film");
         topicsMap.put(12, "Entertainment: Music");
    }

    private void addTopicCard(int topicId, String topicName) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 20);  // Add margin to separate cards
        textView.setLayoutParams(params);
        textView.setText(topicName + " - Click to start quiz");
        textView.setPadding(20, 20, 20, 20);
        textView.setTextSize(18);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.rounded_green_background);  // Use a drawable for rounded corners
        textView.setOnClickListener(v -> onTopicClick(topicId));  // Pass the topicId to onTopicClick

        tasksContainer.addView(textView);
    }

    private void onTopicClick(int topicId) {
        completedTopics.add(topicId); // Mark this topic as completed
        updateTaskCards(); // Refresh the task cards

        // Intent to start QuizActivity, passing the topic ID
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra("topic_id", topicId);
        startActivity(intent);
    }
}
