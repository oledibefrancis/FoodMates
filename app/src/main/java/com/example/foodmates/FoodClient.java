package com.example.foodmates;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class FoodClient extends AsyncHttpClient {
    public static final String API_KEY = "66ed036507b74261af45f98c30aa8f69";
    public static final String RECIPE_SEARCH_URL = String.format("https://api.spoonacular.com/recipes/complexSearch?apiKey=%s", API_KEY);
    public static final String INGREDIENTS_SEARCH_URL = String.format("https://api.spoonacular.com/recipes/findByIngredients/{id}ingredientWidget.json?apiKey=%s", API_KEY);

    public FoodClient() { super(); }

    public void getRecipes(String query,JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("query", query);
        params.put("number", 10);
        get(RECIPE_SEARCH_URL, params, handler);
    }
//    public void getIngredients(JsonHttpResponseHandler handler, int id) {
//        String id_string = String.valueOf(id);
//        String Url = INGREDIENTS_SEARCH_URL.replace("{id}", id_string);
//        get(Url, handler);
//    }

}


