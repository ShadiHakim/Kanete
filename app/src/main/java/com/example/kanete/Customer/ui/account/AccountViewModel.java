package com.example.kanete.Customer.ui.account;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.AuthenticationActivities.LoginActivity;
import com.example.kanete.Models.User;
import com.example.kanete.helper.Utils;

public class AccountViewModel extends ViewModel {

    private final Activity this_activity;
    private MutableLiveData<String> mText;

    public AccountViewModel(Activity activity) {
        this_activity = activity;
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void sign_out(){
        User user = new User();
        user.setThis_activity(this_activity);

        user.signOut();
    }
}