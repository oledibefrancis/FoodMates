package com.example.foodmates.Models;

import com.example.foodmates.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Food implements FeedItem {
    public String title;
    public String imageurl;
    public String id;
    List<Post> posts;

    public Food() {
    }

    public static Food fromJson(JSONObject jsonObject) throws JSONException {
        Food food = new Food();
        food.title = jsonObject.getString("title");
        food.imageurl = jsonObject.getString("image");
        food.id = jsonObject.getString("id");
        return food;
    }

    public static List<Food> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Food> foods = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            foods.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return foods;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getImageUrl() {
        return imageurl;
    }

    @Override
    public String getId() {
        return id;
    }
}
