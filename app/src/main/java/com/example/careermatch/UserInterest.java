package com.example.careermatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class UserInterest extends SignupActivity {

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

        String interest_One = interest1.getText().toString();
        String interest_Two = interest2.getText().toString();
        String interest_Three = interest3.getText().toString();
        getUserInterest(interest_One,interest_Two,interest_Three);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInterest.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //saves the user interest in a database
    private void getUserInterest(String interest1, String interest2, String interest3) {
        ParseObject interest = new ParseObject("Interest");
// Store an object
        interest.put("FirstChoice", interest1);
        interest.put("SecondChoice", interest2);
        interest.put("ThirdChoice", interest3);

// Saving object
        interest.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success
                } else {
                    // Error
                }
            }
        });
    }

}