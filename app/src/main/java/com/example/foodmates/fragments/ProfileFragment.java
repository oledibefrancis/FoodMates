package com.example.foodmates.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodmates.Activities.LoginActivity;
import com.example.foodmates.Activities.SavedPostActivity;
import com.example.foodmates.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2567;
    private static final String TAG = "ProfileFragment";
    public String photoFileName = "photo.jpg";
    Button logOutBtn;
    TextView username;
    TextView profileEmail;
    TextView contactNo;
    TextView birthDate;
    TextView savedPost;
    CircleImageView profilePicture;


    public ProfileFragment() {
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
                if (currentUser != null) {
                    ParseUser.logOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        savedPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SavedPostActivity.class);
                startActivity(intent);
            }
        });

        ParseUser user = ParseUser.getCurrentUser();
        if (user.getParseFile("profilePicture") == null) {
            Glide.with(getContext()).load(getGravatar(user.getObjectId())).into(profilePicture);
        }

        if (user.getParseFile("profilePicture") != null) {
            Glide.with(getContext()).load(user.getParseFile("profilePicture").getUrl()).into(profilePicture);
        }


    }

}