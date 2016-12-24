package com.example.ahmedelkashef.linktask.Fragments;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedelkashef.linktask.CustomLayout;
import com.example.ahmedelkashef.linktask.Models.News;
import com.example.ahmedelkashef.linktask.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.color.holo_orange_light;
import static android.R.color.white;


public class DetailsFragment extends Fragment {

    private String id;
    private TextView titletxt;
    private TextView date;
    private TextView likes;
    private TextView views;
    private TextView description;
    ImageView like_pressed;
    private ProgressDialog progressDialog;
    private News currentNews;
    CustomLayout mCustomLayout;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("newskey", currentNews);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            currentNews = new News();
            id = getActivity().getIntent().getStringExtra("newsid");
            new FetchNewsTask().execute(id);
        } else
            currentNews = savedInstanceState.getParcelable("newskey");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        initialize_data(rootView);
        if (currentNews != null)
            setData(currentNews, rootView);

        like_pressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (like_pressed.getTag().toString().equals("1")) {
                    like_pressed.setImageResource(R.drawable.like_unclicked);
                    likes.setTextColor(getResources().getColor(white));
                    like_pressed.setTag("0");
                } else if ((like_pressed.getTag().toString().equals("0"))) {
                    like_pressed.setImageResource(R.drawable.like_clicked);
                    likes.setTextColor(getResources().getColor(holo_orange_light));
                    like_pressed.setTag("1");
                }

            }
        });

        return rootView;
    }

    public void setData(News news, View V) {
        Picasso.with(getContext()).load(news.getImageUrl()).into(mCustomLayout);

        titletxt.setText(news.getNewsTitle());

        date.setText(news.getPostDate());
        likes.setText("Likes (" + news.getLikes() + ")");
        views.setText(news.getNumofViews() + " views");
        description.setText(news.getItemDescription());
    }

    public void initialize_data(View V) {

        titletxt = (TextView) V.findViewById(R.id.detail_title_txt);
        date = (TextView) V.findViewById(R.id.detail_date_txt);
        likes = (TextView) V.findViewById(R.id.detail_likes_txt);
        views = (TextView) V.findViewById(R.id.detail_view_txt);
        description = (TextView) V.findViewById(R.id.description_txt);
        like_pressed = (ImageView) V.findViewById(R.id.like_icon);
        mCustomLayout = (CustomLayout) V.findViewById(R.id.custom_layout);
    }

    public class FetchNewsTask extends AsyncTask<String, Void, News> {

        private final String LOG_TAG = MainFragment.FetchNewsTask.class.getSimpleName();


        private News getNewsDataFromJson(String NewsJsonStr)
                throws JSONException {

            JSONObject Newsjson = new JSONObject(NewsJsonStr);
            Gson gson = new Gson();
            JSONObject singleJsonObject = Newsjson.getJSONObject("newsItem");
            News news = gson.fromJson(singleJsonObject.toString(), News.class);

            return news;
        }

        @Override
        protected News doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String NewsJsonStr = null;
            try {
                final String BASE_URL = "http://egyptinnovate.com/en/api/v01/safe/GetNewsDetails";
                final String NEWS_ID = "nid";

                Uri BuiltUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(NEWS_ID, strings[0])
                        .build();

                URL url = new URL(BuiltUri.toString());
                Log.v("uri=", BuiltUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }

                NewsJsonStr = buffer.toString();
                Log.v(LOG_TAG, "JSON String = " + NewsJsonStr);
            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            try {
                return getNewsDataFromJson(NewsJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading Please Wait..");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(News News) {


            if (News == null) {
                Toast.makeText(getContext(), "Error in Fetching Data", Toast.LENGTH_SHORT).show();
            }

            currentNews = News;

            Picasso.with(getContext()).load(News.getImageUrl()).into(mCustomLayout);

            titletxt.setText(News.getNewsTitle());

            date.setText(News.getPostDate());
            likes.setText("Likes (" + News.getLikes() + ")");
            views.setText(News.getNumofViews() + " views");
            description.setText(News.getItemDescription());

            progressDialog.dismiss();

        }
    }
}
