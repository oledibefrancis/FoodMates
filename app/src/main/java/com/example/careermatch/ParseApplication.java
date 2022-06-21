package com.example.careermatch;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("HFzeNCio98fUQdfzJWanBDO25UXup9WeyiojCfmh")
                .clientKey("urazO7Umx8Vs3DvO0YmquEyVYVh4iALTVejjkfpg")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
