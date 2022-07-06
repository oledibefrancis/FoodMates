package com.example.foodmates.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.foodmates.Activities.ComposeActivity;
import com.example.foodmates.Activities.UserPostActivity;
import com.example.foodmates.Models.Food;
import com.example.foodmates.Adapters.FoodAdapter;
import com.example.foodmates.Models.UserPost;
import com.example.foodmates.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class HomeFragment extends Fragment {
    public static final String API_URL = "https://api.spoonacular.com/recipes/complexSearch?apiKey=8cf7ac7ac6f449e49a93e9cf5576c873";
    public final static int REQUEST_CODE = 2031;
    List<Food> foods;
    List<UserPost> userPosts;
    RecyclerView rvHomeFeeds;
    android.widget.Toolbar tbHome;
    FloatingActionButton userPostBtn;
    private static final String TAG = "HomeFragment";
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
        userPostBtn = view.findViewById(R.id.userPostBtn);

        foods = new ArrayList<>();
        userPosts = new ArrayList<>();
        foodAdapter = new FoodAdapter(getContext(), foods);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess: " + json.toString());
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    foods.addAll(Food.fromJsonArray(results));
                    foodAdapter = new FoodAdapter(getContext(), foods);
                    rvHomeFeeds.setAdapter(foodAdapter);
                    rvHomeFeeds.setLayoutManager(new LinearLayoutManager(getContext()));
                    //foodAdapter.notifyDataSetChanged();
                    Log.i(TAG,"Results:"+ results.toString());
                } catch (JSONException e) {
                    Log.e("HomeFragment", "Error: " + e.toString());
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"Unable to load api",throwable );
            }
        });

      //  queryPosts();

        userPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserPostActivity.class);
                startActivity(intent);
            }
        });

}

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                foodAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
//
//    private boolean getRecipes(String searchValue) {
//        FoodClient client = new FoodClient();
//        client.getIngredients(selectedDiet, selectedMeal, searchValue, new ConnectivityManager.NetworkCallback<List<Food>>(){
//
//        });
//        return false;
//    }

    public void onCompose(){
        Intent intent = new Intent(getContext(), ComposeActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.addPost){
            onCompose();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void queryPosts(){
        ParseQuery<UserPost> query = ParseQuery.getQuery(UserPost .class);
        query.include(UserPost.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(UserPost.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<UserPost>() {
            @Override
            public void done(List<UserPost> allUserPosts, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Issue with getting posts",e);
                    return;
                }
                for (UserPost userPost : allUserPosts){
                    Log.i(TAG,"Post: " + userPost.getDescription());
                }
            }
        });

    }

}