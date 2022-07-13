package com.example.foodmates.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

        public static final String USER_ID_KEY = "userId";
        public static final String BODY_KEY = "body";

        public String getUserId() {
            return getString(USER_ID_KEY);
        }

        public String getBody() {
            return getString(BODY_KEY);
        }

        public void setUserId(String userId) {
            put(USER_ID_KEY, userId);
        }

        public void setBody(String body) {
            put(BODY_KEY, body);
        }
        //TODO
        //allow users to send image as text
        //check what the user wants to send a text an image or text and put the image into the data base using a setImage function
        //there world be a new relation for the users
        //in the user class there would be a relation of chats they are part of
        //in the chat class there would be a relation to messages

}
