package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.example.foodmates.Adapters.FeedAdapter;
import com.example.foodmates.FeedItem;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SavedPostActivity extends AppCompatActivity {
    public static final String TAG = "savedPostActivity";
    private static final int NUMBER_OF_COLUMNS = 2;

    List<FeedItem> posts;
    FeedAdapter feedAdapter;
    SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_post);

        RecyclerView rvMovies = findViewById(R.id.rvSavedFeeds);
        posts = new ArrayList<>();
        feedAdapter = new FeedAdapter(this, posts);
        rvMovies.setAdapter(feedAdapter);
        rvMovies.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));
        queryPosts();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
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

    private void fetchFeeds() {
        feedAdapter.clear();
        feedAdapter.addAll(posts);
        swipeContainer.setRefreshing(false);
    }

    protected void queryPosts() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<Post> relation = user.getRelation("savedPost");
        ParseQuery<Post> savedQuery = relation.getQuery();
        savedQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        savedQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> savedPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                posts.addAll(savedPosts);
                feedAdapter.notifyDataSetChanged();
            }
        });

    }
}