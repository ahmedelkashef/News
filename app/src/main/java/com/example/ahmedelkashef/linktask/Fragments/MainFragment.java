package com.example.ahmedelkashef.linktask.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ahmedelkashef.linktask.Activities.DetailsActivity;
import com.example.ahmedelkashef.linktask.Adapters.NewsAdapter;
import com.example.ahmedelkashef.linktask.Models.News;
import com.example.ahmedelkashef.linktask.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private ListView newsListView;
    private NewsAdapter newsAdapter;
    private News[] newsArray;
    ProgressDialog progressDialog;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("newsarray")) {
            newsArray = new News[1];

            new FetchNewsTask().execute();
        } else {
            newsArray = (News[]) savedInstanceState.getParcelableArray("newsarray");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray("newsarray", newsArray);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        newsListView = (ListView) rootView.findViewById(R.id.list_view_news);

        if (newsArray.length != 1) {
            newsAdapter = new NewsAdapter(getActivity(), Arrays.asList(newsArray));
            newsListView.setAdapter(newsAdapter);
        }
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News selectedNews = newsArray[i];
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("newsid", selectedNews.getNid());
                startActivity(intent);
            }
        });

        return rootView;
    }


    public class FetchNewsTask extends AsyncTask<Void, Void, News[]> {

        private final String LOG_TAG = FetchNewsTask.class.getSimpleName();


        private News[] getNewsDataFromJson(String NewsJsonStr)
                throws JSONException {
            JSONObject Newsjson = new JSONObject(NewsJsonStr);
            JSONArray jsonArray = Newsjson.getJSONArray("News");
            newsArray = new News[jsonArray.length()];
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                News news = gson.fromJson(singleJsonObject.toString(), News.class);

                newsArray[i] = news;
            }
            return newsArray;
        }

        @Override
        protected News[] doInBackground(Void... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String NewsJsonStr = null;
            try {
                final String BASE_URL = "http://egyptinnovate.com/en/api/v01/safe/GetNews";


                Uri BuiltUri = Uri.parse(BASE_URL).buildUpon()
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


        protected void onPostExecute(News[] News) {


            if (News == null) {
                Toast.makeText(getContext(), "Error in Fetching Data", Toast.LENGTH_SHORT).show();
            }
            newsArray = News;
            newsAdapter = new NewsAdapter(getActivity(), Arrays.asList(News));
            newsListView.setAdapter(newsAdapter);

            progressDialog.dismiss();
        }
    }
}
