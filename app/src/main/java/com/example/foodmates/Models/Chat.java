package com.example.foodmates.Models;

import androidx.annotation.Nullable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Chat")
public class Chat extends ParseObject {


    public static final String KEY_GROUP_NAME = "groupName";
    private static final String KEY_IMAGE = "Image";


    public String getGroupName() {
        return getString(KEY_GROUP_NAME);
    }

    public void setGroupName(String groupName) {
        put(KEY_GROUP_NAME, groupName);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

}
