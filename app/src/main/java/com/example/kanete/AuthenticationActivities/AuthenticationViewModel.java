package com.example.kanete.AuthenticationActivities;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.Customer;
import com.example.kanete.Models.Store;
import com.example.kanete.Models.User;

public class AuthenticationViewModel extends ViewModel {

    private final Activity this_activity;

    public AuthenticationViewModel(Activity activity) {
        this_activity = activity;
    }

    public void signup_auth(User.types type, String email, String password, Customer customer, Store store){
        User user = new User();
        user.setThis_activity(this_activity);

        user.signup_auth(type, email, password, customer, store);
    }

    public LiveData<Boolean> login_auth(String email, String password){
        User user = new User();
        user.setThis_activity(this_activity);

        return user.login_auth(email, password);
    }
}
