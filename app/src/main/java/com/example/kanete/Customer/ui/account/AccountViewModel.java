package com.example.kanete.Customer.ui.account;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.AuthenticationActivities.LoginActivity;
import com.example.kanete.helper.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class AccountViewModel extends ViewModel {

    private final Activity this_activity;
    private MutableLiveData<FirebaseAuth> mAuth;
    private MutableLiveData<String> mText;

    public AccountViewModel(Activity activity) {
        this_activity = activity;
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
        mAuth = new MutableLiveData<>();
        mAuth.setValue(FirebaseAuth.getInstance());
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void sign_out(){
        mAuth.getValue().signOut();
        Utils.goTo(this_activity, LoginActivity.class);
    }
}