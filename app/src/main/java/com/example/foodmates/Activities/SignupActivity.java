package com.example.foodmates.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.example.foodmates.fragments.ChatFragment;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.DateFormat;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "SignUpActivity";

    EditText etUsername;
    EditText etPassword;
    EditText etEmail;
    EditText  etContact;
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
                signUpUser(userName,password,email,contact,birthDate);

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
        finish();
    }

    public void signUpUser(String userName, String password, String email, String contact, String birthDate ) {
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
                Toast.makeText(getApplicationContext(),"Signed up sucessfully" , Toast.LENGTH_SHORT);
                goUserInterest();
            }

            else {
                Log.i(TAG, "onClick Error");
                Toast.makeText(getApplicationContext(),"Error Signing Up" , Toast.LENGTH_SHORT);
                Log.e(TAG, e.toString());
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
    private void showDatePickerDialog(){

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