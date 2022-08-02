package com.example.foodmates.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodmates.Models.Chat;
import com.example.foodmates.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class GroupCreateActivity extends ComposeActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etDescription = findViewById(R.id.etDescription);
        etTitle = findViewById(R.id.etTitle);

        etDescription.setVisibility(View.GONE);
        etTitle.setHint("Enter Group Title....");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.setClickable(false);
                description = etDescription.getText().toString();
                title = etTitle.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(GroupCreateActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    btnSubmit.setClickable(true);
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile, title);
            }
        });
    }

    @Override
    public void savePost(String description, ParseUser currentUser, File photoFile, String title) {
        createChat(title, image);
    }

    private void createChat(String name, ParseFile groupImage) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Chat chat = new Chat();
        chat.setGroupName(name);
        chat.setImage(groupImage);
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {

                    Toast.makeText(GroupCreateActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(GroupCreateActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    btnSubmit.setClickable(true);
                    return;
                }
                etDescription.setText("");
                etTitle.setText("");

                ivPostImage.setImageResource(0);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<Chat> relation = user.getRelation("userGroups");
                relation.add(chat);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                    }
                });
                finish();
            }
        });
    }
}