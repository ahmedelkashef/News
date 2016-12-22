package com.example.ahmedelkashef.linktask.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmedelkashef.linktask.Models.News;
import com.example.ahmedelkashef.linktask.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ahmedelkashef on 12/20/2016.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Activity context , List<News> newses){
        super(context,0,newses);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_news, parent, false);
        }
        final CircleImageView circleImageView = (CircleImageView) convertView.findViewById(R.id.profile_image);
       final ImageView imageView = (ImageView) convertView.findViewById(R.id.play_img);
        final TextView title = (TextView) convertView.findViewById(R.id.title_txt);
        final TextView date = (TextView) convertView.findViewById(R.id.date_txt);
        final TextView likes = (TextView) convertView.findViewById(R.id.likes_txt);
        final TextView views = (TextView) convertView.findViewById(R.id.views_txt);

        Picasso.with(getContext()).load(news.getImageUrl()).into(circleImageView);
        if(news.getNewsType().equals("84"))
        {
            imageView.setImageResource(R.drawable.article_label);
        }
        else
        {
            imageView.setImageResource(R.drawable.video_label);
        }
        title.setText(news.getNewsTitle());

        date.setText(news.getPostDate());

        likes.setText("Likes (" + news.getLikes()+")");

        views.setText(news.getNumofViews() + " views");

        return convertView;
    }
}

