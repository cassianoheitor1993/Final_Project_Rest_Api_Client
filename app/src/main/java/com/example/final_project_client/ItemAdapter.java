package com.example.final_project_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    private Context context;
    private List<Item> items;

    public ItemAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Item item = items.get(position);

        TextView textView1 = view.findViewById(android.R.id.text1);
        textView1.setText(item.getName());

        TextView textView2 = view.findViewById(android.R.id.text2);
        textView2.setText(String.valueOf(item.getPrice()));

        return view;
    }

}
