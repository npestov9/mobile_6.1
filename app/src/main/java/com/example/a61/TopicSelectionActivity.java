package com.example.a61;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.a61.adapters.TopicsAdapter;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class TopicSelectionActivity extends Activity {
    private ListView topicsListView;
    private ArrayAdapter<String> topicsAdapter;
    private ArrayList<String> topicsList;
    private HashSet<String> selectedTopics = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_selection);
        topicsListView = findViewById(R.id.topicsListView);
        topicsList = new ArrayList<>(); // This should be filled with the API's categories
        topicsAdapter = new TopicsAdapter(this, topicsList, selectedTopics);
        topicsListView.setAdapter(topicsAdapter);
        fetchCategories();
    }

    private void fetchCategories() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://opentdb.com/api_category.php";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray categories = jsonResponse.getJSONArray("trivia_categories");
                    ArrayList<String> categoryNames = new ArrayList<>();
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject category = categories.getJSONObject(i);
                        categoryNames.add(category.getString("name"));
                    }
                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(TopicSelectionActivity.this,
                                android.R.layout.simple_list_item_1, categoryNames);
                        topicsListView.setAdapter(adapter);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
