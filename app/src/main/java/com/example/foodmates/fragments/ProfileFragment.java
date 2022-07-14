package com.example.foodmates.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodmates.Activities.ChatActivity;
import com.example.foodmates.Activities.LoginActivity;
import com.example.foodmates.Activities.SavedPostActivity;
import com.example.foodmates.R;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    Button logOutBtn;
    TextView username;
    TextView profileEmail;
    TextView contactNo;
    TextView birthDate;
    TextView savedPost;
    ImageView profilePicture;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private static String getGravatar(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=wavatar";
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
        savedPost = view.findViewById(R.id.savedPost);
        profilePicture = view.findViewById(R.id.profilePicture);

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
//TODO
//        savedPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), SavedPostActivity.class);
//                startActivity(intent);
//            }
//        });
        ParseUser user = ParseUser.getCurrentUser();
        if (user.getString("profilePicture") == null) {
            Glide.with(getContext()).load(getGravatar(user.getObjectId())).into(profilePicture);
        }
        if (user.getString("profilePicture") != null) {
            Glide.with(getContext()).load(user.getString("profilePicture")).into(profilePicture);
        }

    }
}