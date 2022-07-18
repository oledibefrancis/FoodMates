package com.example.foodmates.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodmates.Activities.BreakFastActivity;
import com.example.foodmates.Activities.ChatActivity;
import com.example.foodmates.Models.Chat;
import com.example.foodmates.Models.Message;
import com.example.foodmates.R;
import com.parse.ParseRelation;

public class FoodHubFragment extends Fragment {

    TextView GeneralGroup;
    TextView breakfast;



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

        GeneralGroup = view.findViewById(R.id.GeneralGroup);
        breakfast = view.findViewById(R.id.breakfast);

        GeneralGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });


        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BreakFastActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createChat(String name) {
        Chat chat = new Chat();
        chat.setGroupName(name);
        ParseRelation<Message> relation = chat.getRelation("groupMessages");
        chat.put("groupMessages",relation);
    }

}