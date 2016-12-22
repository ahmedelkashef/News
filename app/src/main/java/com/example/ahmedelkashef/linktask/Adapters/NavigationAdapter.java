package com.example.ahmedelkashef.linktask.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedelkashef.linktask.R;

import java.util.List;

/**
 * Created by ahmedelkashef on 12/20/2016.
 */

public class NavigationAdapter extends ArrayAdapter<String> {

    public NavigationAdapter(Activity context, List<String> objects) {
        super(context, 0, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.navigator_item, parent, false);
        }

        final ImageView imgView = (ImageView) convertView.findViewById(R.id.icon_img);

        TextView titleview = (TextView) convertView.findViewById(R.id.nav_title);
        titleview.setText(item);


        if (item == "NEWS") {
            imgView.setImageResource(R.drawable.news_icon);
        } else if (item == "INNOVATION MAP") {
            imgView.setImageResource(R.drawable.map_icon);
        } else if (item == "EVENT CALENDER") {
            imgView.setImageResource(R.drawable.events_icon);
        } else if (item == "LEADERSHIP THOUGHTS") {
            imgView.setImageResource(R.drawable.leadership_icon);
        } else if (item == "LANGUAGES") {
            imgView.setImageResource(R.drawable.language);
        }

        return convertView;
    }

}

