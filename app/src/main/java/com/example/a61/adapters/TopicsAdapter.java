package com.example.a61.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.a61.R;

import java.util.HashSet;
import java.util.List;

public class TopicsAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> topics;
    private HashSet<String> selectedTopics;

    public TopicsAdapter(Context context, List<String> topics, HashSet<String> selectedTopics) {
        super(context, R.layout.list_item_topic, topics);
        this.context = context;
        this.topics = topics;
        this.selectedTopics = selectedTopics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckBox checkBox;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_topic, parent, false);
        }
        checkBox = convertView.findViewById(R.id.checkbox);
        String topic = topics.get(position);
        checkBox.setText(topic);
        checkBox.setChecked(selectedTopics.contains(topic));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("TopicsAdapter", "Checkbox clicked, isChecked: " + isChecked);
            if (isChecked) {
                selectedTopics.add(topic);
            } else {
                selectedTopics.remove(topic);
            }
        });


        return convertView;
    }
}
