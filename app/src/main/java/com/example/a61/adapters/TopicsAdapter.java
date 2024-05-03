package com.example.a61.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a61.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TopicsAdapter extends ArrayAdapter<String> {
    // Assuming you have a constructor like this
    public TopicsAdapter(Context context, ArrayList<String> topics, HashSet<String> selectedTopics) {
        super(context, R.layout.list_item_topic, topics);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_topic, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.topicTextView);
        Button addButton = convertView.findViewById(R.id.addButton);

        String topic = getItem(position);
        textView.setText(topic);
        addButton.setOnClickListener(v -> {
            // Add click behavior here
            // For example: toggle selection in HashSet, update UI, etc.
        });

        return convertView;
    }
}

