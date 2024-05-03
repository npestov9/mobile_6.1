package com.example.a61.adapters;

import android.content.Context;
import android.graphics.Color;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_topic, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.topicTextView);
        Button addButton = convertView.findViewById(R.id.addButton);
        String topic = getItem(position);

        textView.setText(topic);
        addButton.setFocusable(false);
        addButton.setClickable(true);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                b.setText("Added");
                b.setEnabled(false); // Disable the button after click to avoid multiple clicks
                b.setBackgroundColor(Color.parseColor("#4CAF50")); // Change button color to green
            }
        });

        return convertView;
    }

}
