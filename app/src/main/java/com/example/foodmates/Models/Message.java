package com.example.foodmates.Models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {

    public static final String USER_ID_KEY = "userID";
    public static final String BODY_KEY = "body";
    public static final String USER_ID_POINTER = "userId";


    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public ParseUser getUserIdPointer() {
        ParseUser user = getParseUser(USER_ID_POINTER);
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

    public void setUserIdPointer(ParseUser user) {
        put(USER_ID_POINTER, user);
    }

}
