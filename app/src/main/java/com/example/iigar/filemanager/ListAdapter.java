package com.example.iigar.filemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<File> {
    private int SelectedItem = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File f = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView text = convertView.findViewById(R.id.listview_item_title);
        ImageView img = convertView.findViewById(R.id.listview_item_image);

        text.setText(f.getName());

        if (position == SelectedItem) {
            convertView.setBackgroundResource(R.color.colorAccent);
        } else {
            convertView.setBackgroundResource(R.color.colorItems);
        }

        if (f.isDirectory()) {
            img.setImageResource(R.drawable.ic_folder_black_24dp);
        } else {
            img.setImageResource(R.drawable.ic_weekend_black_24dp);
        }
        return convertView;
    }

    public ListAdapter(Context context, ArrayList<File> users) {
        super(context, 0, users);
    }

    public void setSelectedItem(int item) {
        SelectedItem = item;
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        return SelectedItem;
    }
}
