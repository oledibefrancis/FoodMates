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

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "Loginactivity";

    EditText etUsername;
    EditText etPassword;
    Button etLoginButton;
    Button etSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etSignUp = findViewById(R.id.etSignUp);
        etLoginButton = findViewById(R.id.etLoginButton);

        etLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });




    }

    public void loginUser(String username, String password) {
       //Todo: write the logic
        Log.i(TAG,"Attempting  to login user" + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Issue with login", e);
                    Toast.makeText(LoginActivity.this,"Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this,"Sucess!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    public void onSignUp(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }



}
