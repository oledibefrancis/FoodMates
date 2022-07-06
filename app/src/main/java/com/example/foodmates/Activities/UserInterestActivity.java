package com.example.foodmates.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodmates.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class UserInterestActivity extends SignupActivity {

    EditText interest1;
    EditText interest2;
    EditText interest3;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interest);

        interest1 = findViewById(R.id.interest1);
        interest2 = findViewById(R.id.interest2);
        interest3 = findViewById(R.id.interest3);
        btnSubmit = findViewById(R.id.btnSubmit);

        String interestOne = interest1.getText().toString();
        String interestTwo = interest2.getText().toString();
        String interestThree = interest3.getText().toString();
        getUserInterest(interestOne,interestTwo,interestThree);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInterestActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getUserInterest(String interest1, String interest2, String interest3) {
        ParseObject interest = new ParseObject("Interest");
        interest.put("FirstChoice", interest1);
        interest.put("SecondChoice", interest2);
        interest.put("ThirdChoice", interest3);
        interest.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                } else {
                }
            }
        });
    }

}