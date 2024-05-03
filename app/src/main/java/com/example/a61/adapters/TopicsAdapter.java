package com.example.a61.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.a61.R;

import java.util.List;

public class TopicsAdapter extends ArrayAdapter<String> {
    public TopicsAdapter(Context context, List<String> topics) {
        super(context, 0, topics);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_topic, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.topicTextView);
        Button addButton = convertView.findViewById(R.id.addButton);
        String topic = getItem(position);

        textView.setText(topic);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click here
            }
        });

        return convertView;
    }
}
