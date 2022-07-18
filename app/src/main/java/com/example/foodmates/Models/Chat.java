package com.example.foodmates.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Chat")
public class Chat extends ParseObject{


    public static final String KEY_GROUP_NAME = "groupName";

    public String getGroupName() {
        return getString(KEY_GROUP_NAME);
    }

    public void setGroupName(String groupName) {
        put(KEY_GROUP_NAME, groupName);
    }
}
