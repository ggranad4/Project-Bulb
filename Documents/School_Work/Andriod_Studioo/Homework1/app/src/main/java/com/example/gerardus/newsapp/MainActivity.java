/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.gerardus.newsapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;




import com.example.gerardus.newsapp.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements NewsAdapter.newsListener {

    public RecyclerView mRecycler;

    public NewsAdapter adapt;

    public ArrayList<NewsItem> news;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecycler = (RecyclerView) findViewById(R.id.news_recyclerview);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layout);

//        adapt = new NewsAdapter(this, news);
//        mRecycler.setAdapter(adapt);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String url = news.get(clickedItemIndex).getUrl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }


        /**
         * This method retrieves the search text from the EditText, constructs the
         * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays
         * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
         * our {@link NewsQueryTask}
         */

    // COMPLETED (1) Create a class called GithubQueryTask that extends AsyncTask<URL, Void, String>
    public class NewsQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

     @Override
        protected void onPreExecute() {


     }

    @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
               news = JsonUtils.parseJSON(searchResults);
//               adapt.setNews(news);
 //              mRecycler.setAdapter(adapt);

                adapt = new NewsAdapter(news, MainActivity.this);
                mRecycler.setAdapter(adapt);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.get_news) {
           URL url = NetworkUtils.buildUrl();
           new NewsQueryTask().execute(url);

           return true;
        }
        return super.onOptionsItemSelected(item);
    }

}




