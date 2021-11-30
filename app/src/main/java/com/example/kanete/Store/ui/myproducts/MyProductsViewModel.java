package com.example.kanete.Store.ui.myproducts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyProductsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyProductsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MyProducts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}