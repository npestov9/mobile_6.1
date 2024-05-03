package com.example.a61;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.a61.R;
import com.example.a61.adapters.TopicsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopicSelectionActivity extends Activity {
    private ListView topicsListView;
    private TopicsAdapter topicsAdapter;
    private List<String> topicsList = new ArrayList<>();
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_selection);
        confirmBtn = findViewById((R.id.confirmButton));

        topicsListView = findViewById(R.id.topicsListView);
        topicsAdapter = new TopicsAdapter(this, topicsList);
        topicsListView.setAdapter(topicsAdapter);

        fetchCategories();

        confirmBtn.setOnClickListener(v -> {
            Intent intent = new Intent(TopicSelectionActivity.this, DashboardActivity.class);
            intent.putStringArrayListExtra("selectedTopics", new ArrayList<>(topicsList)); // Ensure this matches what DashboardActivity expects
            startActivity(intent);
        });
    }

    private void fetchCategories() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://opentdb.com/api_category.php";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray categories = jsonResponse.getJSONArray("trivia_categories");
                    List<String> categoryNames = new ArrayList<>();
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject category = categories.getJSONObject(i);
                        categoryNames.add(category.getString("name"));
                    }
                    runOnUiThread(() -> {
                        topicsList.clear();
                        topicsList.addAll(categoryNames);
                        topicsAdapter.notifyDataSetChanged(); // Refresh the list with new data
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

