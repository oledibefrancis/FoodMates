package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodmates.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.DateFormat;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "SignUpActivity";

    EditText etUsername;
    EditText etPassword;
    EditText etEmail;
    EditText etContact;
    TextView etBirthDate;
    Button etSignUpButton;
    Button loginButton;
    ImageView iconDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loginButton = findViewById(R.id.loginButton);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etSignUpButton = findViewById(R.id.etSignUpButton);
        etEmail = findViewById(R.id.etEmail);
        etContact = findViewById(R.id.etContact);
        etBirthDate = findViewById(R.id.etBirthDate);
        iconDate = findViewById(R.id.iconDate);


        etSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String userName = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String contact = etContact.getText().toString();
                String birthDate = etBirthDate.getText().toString();


                if (userName.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    etSignUpButton.setClickable(true);
                    return;
                } else if (email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                    etSignUpButton.setClickable(true);
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    etSignUpButton.setClickable(true);
                    return;
                } else if (contact.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Contact cannot be empty", Toast.LENGTH_SHORT).show();
                    etSignUpButton.setClickable(true);
                    return;
                } else if (birthDate.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Birthdate cannot be empty", Toast.LENGTH_SHORT).show();
                    etSignUpButton.setClickable(true);
                    return;
                } else {
                    signUpUser(userName, password, email, contact, birthDate);
                }

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        iconDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }

    private void goUserInterest() {
        Intent intent = new Intent(this, UserInterestActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void signUpUser(String userName, String password, String email, String contact, String birthDate) {
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.put("Date", birthDate);
        user.put("Contact", contact);


        user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.i(TAG, "onClick signUp button");
                                            Toast.makeText(getApplicationContext(), "Signed up sucessfully", Toast.LENGTH_SHORT);
                                            goUserInterest();
                                        } else {
                                            Log.i(TAG, "onClick Error");
                                            Toast.makeText(SignupActivity.this, "Error Signing Up", Toast.LENGTH_SHORT);
                                            Log.e(TAG, e.toString());
                                            Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_SHORT);

                                        }
                                    }
                                }
        );
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());
        etBirthDate.setText(currentDateString);
    }

    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}