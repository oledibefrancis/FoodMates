package com.example.foodmates.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.foodmates.Activities.ComposeActivity;
import com.example.foodmates.Adapters.PostAdapter;
import com.example.foodmates.Models.Food;
import com.example.foodmates.Adapters.FoodAdapter;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class HomeFragment extends Fragment {
    public static final String API_URL2 = "https://api.spoonacular.com/recipes/findByNutrients?apiKey=8cf7ac7ac6f449e49a93e9cf5576c873&minCarbs=10&maxCarbs=50&number=10";
    public static final String API_URL = "https://api.spoonacular.com/recipes/complexSearch?apiKey=8cf7ac7ac6f449e49a93e9cf5576c873";
    public final static int REQUEST_CODE = 2031;
    List<Post> posts;
    List<Food> foods;
    RecyclerView rvHomeFeeds;
    android.widget.Toolbar tbHome;
    private static final String TAG = "HomeFragment";
    PostAdapter postAdapter;
    SwipeRefreshLayout swipeContainer;
    FoodAdapter foodAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setActionBar(tbHome);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHomeFeeds = view.findViewById(R.id.rvHomeFeeds);
        tbHome = view.findViewById(R.id.tbHome);
//        apiCall();

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(),posts);
        rvHomeFeeds.setAdapter(postAdapter);
        rvHomeFeeds.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();


        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeeds();
                queryPosts();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Type here to search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchResults(query);
                posts.clear();//clear array before adding result
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                foodAdapter.getFilter().filter(newText);
                return false;
            }
        });



    }

    public static Food fromJson(JSONObject jsonObject) throws JSONException {
        Food food = new Food();
        food.title = jsonObject.getString("title");
        food.imageurl = jsonObject.getString("image");
//        food.calories = jsonObject.getString("calories");
//        food.carbs = jsonObject.getString("carbs");
//        food.fat = jsonObject.getString("fat");
//        food.protein = jsonObject.getString("protein");
        return food;
    }

    private boolean getSearchResults(String searchValue) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_URL+"&query="+searchValue, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                Log.d(TAG, "onSuccess: " + json.toString());
                JSONObject jsonObject = json.jsonObject;
                foods = new ArrayList<>();
//                JSONArray jsonArray = json.jsonArray;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results);
                    foods.addAll(Food.fromJsonArray(results));
                    foodAdapter = new FoodAdapter(getContext(), foods);
//                   saveInDataBase(results);
                    foodAdapter.notifyDataSetChanged();
                    rvHomeFeeds.setAdapter(foodAdapter);
                    rvHomeFeeds.setLayoutManager(new LinearLayoutManager(getContext()));
                    Log.i(TAG, "food: " + foods.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }

            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
        return false;
    }

    private void saveInDataBase(JSONArray results) {
        for (int i = 0 ; i <=results.length(); i++){
            try {
                foods.add(fromJson(results.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Post post = new Post();
            post.setTitle(foods.get(foods.size()-1).getTitle());
            post.setImageUrl(foods.get(foods.size()-1).getImageurl());
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getActivity(), "Successfully saved api result to database", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.e(TAG,"Failed to save api result to database");
                        Log.e(TAG,e.toString());
                    }
                }
            });
        }
    }

    public void apiCall() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_URL2, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess: " + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    JSONArray results = jsonArray;
                    foods = new ArrayList<>();
                    foods.addAll(Food.fromJsonArray(results));
                    Log.i(TAG, "Results: " + results.length());
                    saveInDataBase(results);
                } catch (JSONException e) {
                    Log.e("HomeFragment", "Error Storing api data: " + e.toString());
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Unable to load api", throwable);
            }
        });

    }


    public void onCompose() {
        Intent intent = new Intent(getContext(), ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.addPost) {
            onCompose();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void fetchFeeds() {
        postAdapter.clear();
        postAdapter.addAll(posts);
        swipeContainer.setRefreshing(false);
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(50);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> dataPosts, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Issue with getting posts", e);
                    return;
                }
                for(Post post : dataPosts){
                    Log.i(TAG, "Post" + post.getKeyTitle());
                }
                posts.addAll(dataPosts);
                postAdapter.notifyDataSetChanged();
            }
        });

    }
}

