package com.example.hanium_login_trail2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    // ############################################################# View Components
    TextView txtNotAccount;     // For creating account
    //TextView txtForgetPass;     // For retrieving password
    Button btnLogin;            // Button for Login
    EditText etUsername;
    EditText etPassword;
    // ############################################################# End View Components

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        initViewComponents();
    }

    private void initViewComponents(){
        txtNotAccount = findViewById(R.id.txtNotAccount);
        //txtForgetPass= findViewById(R.id.txtForgetPass);
        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        txtNotAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { // 회원가입창으로 intent
                startActivity(new Intent(Login.this, Signup.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cognito authentication = new Cognito(getApplicationContext());
                authentication.userLogin(etUsername.getText().toString().replace(" ", ""), etPassword.getText().toString());
            }
        });
    }
}
