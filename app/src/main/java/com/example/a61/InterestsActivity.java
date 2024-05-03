package com.example.a61;

// InterestsActivity.java
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class InterestsActivity extends Activity {
    private ListView interestsListView;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        interestsListView = (ListView) findViewById(R.id.interestsListView);
        nextButton = (Button) findViewById(R.id.nextButton);

        // Dummy data for list of interests
        String[] interests = new String[]{"Algorithms", "Data Structures", "Web Development", "Testing"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, interests);
        interestsListView.setAdapter(adapter);

        nextButton.setOnClickListener(v -> {
            // Navigate or do something on click
            // For now, just finish the activity
            finish();
        });
    }
}
