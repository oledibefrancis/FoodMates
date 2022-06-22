package com.example.careermatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

    EditText etUsername;
    EditText etPassword;
    EditText etEmail;
    Button etSignUpButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etSignUpButton = findViewById(R.id.etSignUpButton);
        etEmail = findViewById(R.id.etEmail);


        etSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String userName = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signUpUser(userName,password,email);

            }
        });


    }

    private void goUserInterest() {
        Intent intent = new Intent(this, UserInterest.class);
        startActivity(intent);
        finish();
    }

    private void signUpUser(String userName, String password, String email) {
        ParseUser user = new ParseUser();

        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
            if (e == null) {
                Log.i(TAG, "onClick signUp button");
                Toast.makeText(getApplicationContext(),"Signed up sucessfully" , Toast.LENGTH_SHORT);
                goUserInterest();
            }
            else {
                // Sign up did not succeed.
                Toast.makeText(getApplicationContext(),"Error Signing Up" , Toast.LENGTH_SHORT);
            }

        }
    }
        );

    }



    public void onLogIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}