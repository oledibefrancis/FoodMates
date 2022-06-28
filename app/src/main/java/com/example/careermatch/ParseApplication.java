package com.example.careermatch;

import android.app.Application;

import com.example.careermatch.Models.Message;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.livequery.ParseLiveQueryClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("oYLX0MmEMMXB8aUaDsqt0jHtJ9KODKGGNEkI8uPI")
                .clientKey("5xvKWdoy0AxjyId4kBmO4mk1jXIeWe3jKWEHVmmU")
                .server("https://parseapi.back4app.com")
                .build()
        );
        ParseObject.registerSubclass(Message.class);




    }
}
