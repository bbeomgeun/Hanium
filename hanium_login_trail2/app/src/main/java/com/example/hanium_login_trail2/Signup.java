package com.example.hanium_login_trail2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signup extends AppCompatActivity {
    // ############################################################# View Components
    EditText etUsername;        // Enter Username
    EditText etEmail;           // Enter Email
    EditText etMobile;          // Enter Mobile
    EditText etPass;            // Enter Password
    EditText etRepeatPass;      // Repeat Password
    EditText etConfCode;        // Enter Confirmation Code

    Button btnSignUp;           // Sending data to Cognito for registering new user
    Button btnVerify;
    // ############################################################# End View Components

    // ############################################################# Cognito connection
    Cognito authentication;
    private String userId;
    // ############################################################# End Cognito connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authentication = new Cognito(getApplicationContext());
        initViewComponents();
    }

    private void initViewComponents(){
        etUsername = findViewById(R.id.etUsername);
        etEmail= findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPass = findViewById(R.id.etPass);
        etRepeatPass = findViewById(R.id.etRepeatPass);
        etConfCode = findViewById(R.id.etConfCode);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnVerify = findViewById(R.id.btnVerify);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPass.getText().toString().endsWith(etRepeatPass.getText().toString())){
                    userId = etUsername.getText().toString().replace(" ", "");
                    authentication.addAttribute("name", userId);
                    authentication.addAttribute("phone_number", etMobile.getText().toString().replace(" ", ""));
                    authentication.addAttribute("email", etEmail.getText().toString().replace(" ", ""));
                    authentication.signUpInBackground(userId, etPass.getText().toString());
                }
                else{

                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication.confirmUser(userId, etConfCode.getText().toString().replace(" ", ""));
                //finish();
            }
        });

    }
}