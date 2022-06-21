package com.example.careermatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignActivity";

    EditText etUsername;
    EditText etPassword;
    EditText etEmail;
    Button etSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etSignUp = findViewById(R.id.etSignUp);
        etEmail = findViewById(R.id.etEmail);


        etSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new parse user object:
                ParseUser user = new ParseUser();
                // Set details:
                String email = etEmail.getText().toString();
                String userName = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                user.setUsername(userName);
                user.setPassword(password);
                user.setEmail(email);
                // Invoke signUpInBackground:
                user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i(TAG, "onClick signUp button");
                        Toast.makeText(getApplicationContext(),"Signed up sucessfully" , Toast.LENGTH_SHORT);

                    }
                    else {
                        // Sign up did not succeeed.
                        Toast.makeText(getApplicationContext(),"Error Signing Up" , Toast.LENGTH_SHORT);
                    }

                }
            }
                );
            }
        });


    }

    public void onLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}