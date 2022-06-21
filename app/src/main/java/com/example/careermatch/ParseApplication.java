package com.example.careermatch;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("oYLX0MmEMMXB8aUaDsqt0jHtJ9KODKGGNEkI8uPI")
                .clientKey("5xvKWdoy0AxjyId4kBmO4mk1jXIeWe3jKWEHVmmU")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
