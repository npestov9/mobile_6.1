package com.example.a61.adapters;

import android.content.Context;
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
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_item_topic, parent, false);
        }

        CheckBox checkBox = view.findViewById(R.id.checkbox);
        String topic = topics.get(position);
        checkBox.setText(topic);
        checkBox.setChecked(selectedTopics.contains(topic));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedTopics.add(topic);
            } else {
                selectedTopics.remove(topic);
            }
        });

        return view;
    }
}
