package com.example.foodmates.fragments;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.foodmates.Adapters.GroupChatAdapter;
import com.example.foodmates.Models.Chat;
import com.example.foodmates.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class FoodHubFragment extends Fragment {

    private static final String TAG = "FoodHubFragment";
    List<Chat> chats;
    RecyclerView rvGroupChat;
    GroupChatAdapter groupChatAdapter;


    public FoodHubFragment() {
    }

    public static FoodHubFragment newInstance(String param1, String param2) {
        FoodHubFragment fragment = new FoodHubFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_hub, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvGroupChat = view.findViewById(R.id.rvGroupChat);
        chats = new ArrayList<>();
        groupChatAdapter = new GroupChatAdapter(getContext(), chats);

        rvGroupChat.setAdapter(groupChatAdapter);
        rvGroupChat.setLayoutManager(new LinearLayoutManager(getContext()));
        queryGroups();

    }

    public void queryGroups() {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.findInBackground(new FindCallback<Chat>() {
            @Override
            public void done(List<Chat> groups, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                chats.addAll(groups);
                groupChatAdapter.notifyDataSetChanged();
            }
        });
    }
}