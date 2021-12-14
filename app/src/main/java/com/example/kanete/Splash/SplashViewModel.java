package com.example.kanete.Splash;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.example.kanete.AuthenticationActivities.LoginActivity;
import com.example.kanete.Models.User;
import com.example.kanete.helper.Utils;

public class SplashViewModel extends ViewModel {

    private final Activity this_activity;
    private User user;

    public SplashViewModel(Activity activity) {
        this_activity = activity;
        user = new User();
        user.setThis_activity(this_activity);
    }

    public void goToRelevantActivity(){
        if (user.isLoggedIn()){
            type_login();
        }
        else {
            Utils.goTo_emptyStack(this_activity, LoginActivity.class);
        }
    }

    public void type_login(){
        user.type_login();
    }
}
