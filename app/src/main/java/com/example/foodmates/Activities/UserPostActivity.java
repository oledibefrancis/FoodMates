package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.example.foodmates.Models.UserPost;
import com.example.foodmates.R;
import com.example.foodmates.UserPostAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserPostActivity extends AppCompatActivity {

    private static final String TAG ="UserPostActivity" ;
    private RecyclerView rvPosts;
    protected UserPostAdapter adapter;
    protected List<UserPost> allPosts;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        rvPosts = findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        adapter = new UserPostAdapter(this,allPosts);

        rvPosts.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(linearLayoutManager);


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
        adapter.clear();
        adapter.addAll(allPosts);
        swipeContainer.setRefreshing(false);
    }

    protected void queryPosts() {
        ParseQuery<UserPost> query = ParseQuery.getQuery(UserPost.class);
        query.include(UserPost.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(UserPost.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<UserPost>() {
            @Override
            public void done(List<UserPost> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Issue with getting posts", e);
                    return;
                }
                for(UserPost post : posts){
                    Log.i(TAG, "Post" + post.getDescription()+ post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });

    }
}