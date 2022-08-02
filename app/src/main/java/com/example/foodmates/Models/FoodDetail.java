package com.example.foodmates.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class FoodDetail {

    public String summary;

    public FoodDetail() {
    }


    public FoodDetail(JSONObject jsonObject) throws JSONException {
        summary = jsonObject.getString("summary");
    }

    public static FoodDetail fromJsonObject(JSONObject object) throws JSONException {
        return new FoodDetail(object);
    }

    public String getDetail() {
        return summary;
    }

}
