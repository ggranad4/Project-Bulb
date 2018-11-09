package com.example.gerardus.newsapp;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class JsonUtils {

    public static ArrayList<NewsItem> parseJSON(String searchResults) {

        ArrayList<NewsItem> items = new ArrayList<>();
        try {
            JSONObject JSONObject = new JSONObject(searchResults);
            JSONArray news_items = JSONObject.getJSONArray("articles");

            for (int i = 0; i < news_items.length(); i++) {
                JSONObject news_item = news_items.getJSONObject(i);
                items.add(new NewsItem(news_item.getString("title"),
                        news_item.getString("description"),
                        news_item.getString("url"),
                        news_item.getString("publishedAt")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }
}

