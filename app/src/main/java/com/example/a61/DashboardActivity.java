package com.example.a61;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardActivity extends Activity {
    private TextView welcomeTextView;
    private TextView taskCountTextView;
    private List<String> topics = new ArrayList<>();
    private List<String> selectedTopics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeTextView = findViewById(R.id.welcomeTextView);
        taskCountTextView = findViewById(R.id.taskCountTextView);

        String userName = "Your Name"; // Ideally, this should be dynamically fetched based on user session
        welcomeTextView.setText("Hello, " + userName);

        // Dummy data for topics
        topics.add("Math");
        topics.add("Science");
        topics.add("History");
        topics.add("Literature");
        topics.add("Geography");

        // Select 3 random topics
        selectRandomTopics(3);

        updateTaskCards();
    }

    private void selectRandomTopics(int count) {
        Collections.shuffle(topics);
        selectedTopics = topics.subList(0, count);
    }

    public void updateTaskCards() {
        taskCountTextView.setText("You have " + selectedTopics.size() + " tasks due");

        for (int i = 1; i <= selectedTopics.size(); i++) {
            int resId = getResources().getIdentifier("generatedTask" + i, "id", getPackageName());
            TextView taskView = findViewById(resId);
            if (taskView != null) {
                taskView.setText(selectedTopics.get(i-1) + " - Quiz on this topic");
            } else {
                // Log or handle the case where the task view is not found
                Log.e("DashboardActivity", "Task TextView not found for ID: generatedTask" + i);
            }
        }
    }


    public void onTaskClick(View view) {
        // Determine which task was clicked
        String topic = ((TextView)view).getText().toString().split(" - ")[0];
        Intent intent = new Intent(DashboardActivity.this, QuizActivity.class);
        intent.putExtra("topic", topic);

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle quiz completion
        if (resultCode == Activity.RESULT_OK) {
            String completedTopic = data.getStringExtra("completedTopic");
            selectedTopics.remove(completedTopic);
            updateTaskCards(); // Refresh task cards
        }
    }
}
