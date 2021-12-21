package com.example.kanete.Store.ui.account;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.User;

public class AccountStoreViewModel extends ViewModel {

    private final Activity this_activity;

    public AccountStoreViewModel(Activity activity) {
        this_activity = activity;
    }

    public void sign_out(){
        User user = new User();
        user.setThis_activity(this_activity);

        user.signOut();
    }
}