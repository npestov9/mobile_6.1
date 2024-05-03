package com.example.a61;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.a61.R;

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
    private List<String> topicsList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_selection);

        topicsListView = findViewById(R.id.topicsListView);
        adapter = new ArrayAdapter<>(this, R.layout.list_item_topic, R.id.topicTextView, topicsList);
        topicsListView.setAdapter(adapter);

        // Setup item click listener to handle the button inside each item
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Button addButton = view.findViewById(R.id.addButton);
                addButton.setOnClickListener(v -> {
                    // Handle the addition logic here
                    // Example: add topic to selected list or display a toast
                });
            }
        });

        fetchCategories();
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
                        adapter.notifyDataSetChanged(); // Refresh the list with new data
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
