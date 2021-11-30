package com.example.kanete.Store.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountStoreViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccountStoreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is a store account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}