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


    //TODO
    //allow users to send image as text
    //check what the user wants to send a text an image or text and put the image into the data base using a setImage function
    //there world be a new relation for the users
    //in the user class there would be a relation of chats they are part of
    //in the chat class there would be a relation to messages

}
