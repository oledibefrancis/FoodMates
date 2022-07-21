package com.example.foodmates;

import android.app.Application;

import com.example.foodmates.Models.Chat;
import com.example.foodmates.Models.Message;
import com.example.foodmates.Models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Chat.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("oYLX0MmEMMXB8aUaDsqt0jHtJ9KODKGGNEkI8uPI")
                .clientKey("5xvKWdoy0AxjyId4kBmO4mk1jXIeWe3jKWEHVmmU")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
