package com.example.careermatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseUser;

public class UserInterest extends AppCompatActivity {

    EditText interest1;
    EditText interest2;
    EditText interest3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interest);

        interest1= findViewById(R.id.interest1);
        interest2 = findViewById(R.id.interest2);
        interest3 = findViewById(R.id.interest3);

        String interest_One = interest1.getText().toString();
        String interest_Two = interest2.getText().toString();
        String interest_Three = interest3.getText().toString();
        getUserInterest(interest_One,interest_Two,interest_Three);
        onSubmit();


    }

    private void getUserInterest(String intrest1, String intrest2, String intrest3) {
        ParseUser user = new ParseUser();
        user.setUsername(intrest1);
        user.setPassword(intrest2);
        user.setEmail(intrest2);
    }


    public void onSubmit() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}