package com.example.foodmates.Models;

import android.util.Log;

import com.example.foodmates.FeedItem;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@ParseClassName("Post")
public class Post extends ParseObject implements FeedItem {
    public static final String KEY_IMAGE = "image";
    public static final String KEY_IMAGE_URL = "imageurl";
    public static final String KEY_USER = "user";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ID = "postId";
    public static final String KEY_DETAILS = "details";


    public Post() {//default constructor
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }


    @Override
    public String getImageUrl() {
        return getString(KEY_IMAGE_URL);
    }

    public void setImageUrl(String imageUrl) {
        put(KEY_IMAGE_URL, imageUrl);
    }

    public ParseUser getUser() {
        ParseUser user = getParseUser(KEY_USER);
        if (user != null) {
            try {
                assert user != null;
                user.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    @Override
    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    @Override
    public String getId() {
        return getString(KEY_ID);
    }

    public void setId(String id) {
        put(KEY_ID, id);
    }

    public String getDetail() {
        return getString(KEY_DETAILS);
    }

    public void setDetail(String detail) {
        put(KEY_DETAILS, detail);
    }


    public String calculateTimeAgo() {
        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;
        int WEEK_MILLIS = 7 * DAY_MILLIS;

        try {
            Date createdAt = getCreatedAt();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else if (diff / DAY_MILLIS > 7) {
                return "a week";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

}
