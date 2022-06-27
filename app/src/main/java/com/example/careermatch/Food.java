package com.example.careermatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Food {
    public String title;
    public String imageurl;


    public Food() {

    }

    public static Food fromJson(JSONObject jsonObject) throws JSONException {
        Food food = new Food();
        food.title = jsonObject.getString("title");
        food.imageurl = jsonObject.getString("image");
        return food;
    }

    public  static List<Food> fromJsonArray(JSONArray jsonArray) throws JSONException {
         List<Food> foods = new ArrayList<>();
         for (int i = 0 ; i < jsonArray.length(); i++){
             foods.add(fromJson(jsonArray.getJSONObject(i)));
         }
         return foods;
    }

    public  String getTitle(){return title;}

    public String getImageurl(){return imageurl;}
}
