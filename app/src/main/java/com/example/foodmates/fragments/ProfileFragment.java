package com.example.foodmates.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodmates.Activities.LoginActivity;
import com.example.foodmates.R;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    Button logOutBtn;
    TextView username;
    TextView profileEmail;
    TextView contactNo;
    TextView birthDate;




    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logOutBtn = view.findViewById(R.id.logOutBtn);
        username = view.findViewById(R.id.username);
        profileEmail = view.findViewById(R.id.profileEmail);
        contactNo = view.findViewById(R.id.contactNo);
        birthDate = view.findViewById(R.id.birthDate);

        username.setText(ParseUser.getCurrentUser().getUsername());
        profileEmail.setText(ParseUser.getCurrentUser().getEmail());
        contactNo.setText(ParseUser.getCurrentUser().getString("Contact"));
        birthDate.setText(ParseUser.getCurrentUser().getString("Date"));


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}