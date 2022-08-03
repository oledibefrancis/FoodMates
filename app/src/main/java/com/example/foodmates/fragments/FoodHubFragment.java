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

import com.example.foodmates.Adapters.GroupChatAdapter;
import com.example.foodmates.Activities.GroupCreateActivity;
import com.example.foodmates.Models.Chat;
import com.example.foodmates.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FoodHubFragment extends Fragment {

    private static final String TAG = "FoodHubFragment";
    List<Chat> chats;
    List<Chat> suggestedChat;
    RecyclerView rvGroupChat;
    RecyclerView rvSuggestedGroups;
    GroupChatAdapter groupChatAdapter;
    GroupChatAdapter suggestedChatAdapter;
    android.widget.Toolbar tbFoodHub;


    public FoodHubFragment() {
    }

    public static FoodHubFragment newInstance(String param1, String param2) {
        FoodHubFragment fragment = new FoodHubFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setActionBar(tbFoodHub);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_hub, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tbFoodHub = view.findViewById(R.id.tbFoodHub);
        rvGroupChat = view.findViewById(R.id.rvGroupChat);
        rvSuggestedGroups = view.findViewById(R.id.rvSuggestedGroups);

        chats = new ArrayList<>();
        suggestedChat = new ArrayList<>();
        groupChatAdapter = new GroupChatAdapter(getContext(), chats);
        suggestedChatAdapter = new GroupChatAdapter(getContext(), suggestedChat);


        rvGroupChat.setAdapter(groupChatAdapter);
        rvGroupChat.setLayoutManager(new LinearLayoutManager(getContext()));
        queryGroups(new queryGroupsListener() {
            @Override
            public void onComplete() {
                rvSuggestedGroups.setAdapter(suggestedChatAdapter);
                rvSuggestedGroups.setLayoutManager(new LinearLayoutManager(getContext()));
                querySuggestedGroups();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_foodhub, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addGroup:
                groupAdd();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void groupAdd() {
        Intent intent = new Intent(getContext(), GroupCreateActivity.class);
        startActivity(intent);
    }

    public void queryGroups(queryGroupsListener listener) {
        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<Chat> relation = user.getRelation("userGroups");
        ParseQuery<Chat> query = relation.getQuery();
        query.findInBackground(new FindCallback<Chat>() {
            @Override
            public void done(List<Chat> groups, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    listener.onComplete();
                    return;
                }
                chats.addAll(groups);
                groupChatAdapter.notifyDataSetChanged();
                listener.onComplete();
            }
        });
    }

    public void querySuggestedGroups() {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        List<String> l = new ArrayList<>();
        for (Chat c : chats) {
            l.add(c.getObjectId());
        }
        query.whereNotContainedIn("objectId", l);
        query.findInBackground(new FindCallback<Chat>() {
            @Override
            public void done(List<Chat> groups, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                suggestedChat.addAll(groups);
                suggestedChatAdapter.notifyDataSetChanged();
            }
        });
    }


    // Listener for queryGroups callback
    public interface queryGroupsListener {
        void onComplete();
    }

}