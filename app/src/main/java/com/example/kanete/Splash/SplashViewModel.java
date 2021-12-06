package com.example.kanete.Splash;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.AuthenticationActivities.LoginActivity;
import com.example.kanete.Customer.CustomerMainActivity;
import com.example.kanete.Models.User;
import com.example.kanete.Store.StoreMainActivity;
import com.example.kanete.helper.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
            Utils.goTo(this_activity, LoginActivity.class);
        }
    }

    public void type_login(){
        user.type_login();
    }
}
