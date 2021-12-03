package com.example.kanete.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kanete.R;

public class SplashActivity extends AppCompatActivity {

    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        splashViewModel = new SplashViewModel(this);
        splashViewModel.goToRelevantActivity();
    }
}