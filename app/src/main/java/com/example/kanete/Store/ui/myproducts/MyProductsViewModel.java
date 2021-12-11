package com.example.kanete.Store.ui.myproducts;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.Category;
import com.example.kanete.Models.Product;

import java.util.List;

public class MyProductsViewModel extends ViewModel {

    private Activity this_activity;
    private Product product;
    private MutableLiveData<List<Product>> products;

    public MyProductsViewModel(Activity activity) {
        this_activity = activity;
        product = new Product();
        products = product.getMyProducts();//TODO get by chunks
    }

    public LiveData<List<Product>> getMyProducts() {
        return products;
    }
}