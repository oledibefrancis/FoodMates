package com.example.foodmates.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Food {
    public String title;
    public String imageurl;
    public String calories;
    public String carbs;
    public String fat;
    public String protein;
    List<Post> posts;

    public Food() {
    }

    public static Food fromJson(JSONObject jsonObject) throws JSONException {
        Food food = new Food();
        food.title = jsonObject.getString("title");
        food.imageurl = jsonObject.getString("image");
        //TODO
//        food.calories = jsonObject.getString("calories");
//        food.carbs = jsonObject.getString("carbs");
//        food.fat = jsonObject.getString("fat");
//        food.protein = jsonObject.getString("protein");
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

    public String getCalories(){return calories;}

    public String getCarbs(){return carbs;}

    public String getFat(){return fat;}

    public String getProtein(){ return protein; }

    //create a class to store api information
    //store title, imageurl or other details as columns or stores as an array for each title
    //use getTtile with the class put statement to store the details in the parse database
}
