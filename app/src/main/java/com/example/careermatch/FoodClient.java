package com.example.careermatch;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.BuildConfig;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

//public class foodclient extends AsyncHttpClient {
//    public static final String API_KEY = "f1bb97f5a6b141f1b5f8e17a2eba1296";
//    public static final String RECIPE_SEARCH_URL = String.format("https://api.spoonacular.com/recipes/complexSearch?apiKey=%s", API_KEY);
//    public static final String INGREDIENTS_SEARCH_URL = String.format("https://api.spoonacular.com/recipes/findByIngredients/{id}ingredientWidget.json?apiKey=%s", API_KEY);
//
//    public foodclient() { super(); }
//
//    public void getRecipes(JsonHttpResponseHandler handler, String query) {
//        RequestParams params = new RequestParams();
//        params.put("query", query);
//        params.put("number", 10);
//        get(RECIPE_SEARCH_URL, params, handler);
//    }
//    public void getIngredients(JsonHttpResponseHandler handler, int id) {
//        String id_string = String.valueOf(id);
//        String Url = INGREDIENTS_SEARCH_URL.replace("{id}", id_string);
//        get(Url, handler);
//    }
//
//}

public class FoodClient extends AsyncHttpClient{
//    public static final String API_KEY = "AIzaSyAPRoUKxHIMM3GguTKna1hS4Yd_v5kzc5s";
//    public static final String GOOGLE_BOOK_URL = String.format("https://www.googleapis.com/books/v1/volumes", API_KEY);
//
//    public FoodClient(){super();}
//
//        public void getBooks(JsonHttpResponseHandler handler, String search) {
//        RequestParams params = new RequestParams();
//        params.put("search", search);
////        params.put("number", 10);
//        get(GOOGLE_BOOK_URL, params, handler);
//    }

}
