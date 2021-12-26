package com.example.kanete.AuthenticationActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kanete.Customer.CustomerMainActivity;
import com.example.kanete.R;
import com.example.kanete.Store.StoreMainActivity;

public class LoginActivity extends AppCompatActivity {

    private AuthenticationViewModel authenticationViewModel;

    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;

    private Button buttonLogin;
    private TextView textViewSignup;
    private ProgressBar progressBarlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    public void init(){
        authenticationViewModel = new AuthenticationViewModel(this);
        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup = findViewById(R.id.textViewSignup);
        progressBarlogin = findViewById(R.id.progressBarlogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void login() {
        boolean flag;
        String email = editTextLoginEmail.getText().toString();
        String password = editTextLoginPassword.getText().toString();

        flag = AuthenticationUtils.checkEmail(email, editTextLoginEmail);
        flag &= AuthenticationUtils.checkPassword(password, editTextLoginPassword);

        if (flag){
            progressBarlogin.setVisibility(View.VISIBLE);
            authenticationViewModel.login_auth(email, password).observeForever(new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    progressBarlogin.setVisibility(View.GONE);
                }
            });
        }
    }
}