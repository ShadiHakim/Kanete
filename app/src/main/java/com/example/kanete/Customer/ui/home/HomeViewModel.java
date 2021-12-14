package com.example.kanete.Customer.ui.home;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.Category;
import com.example.kanete.Models.Product;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private Activity this_activity;
    private Category category;
    private MutableLiveData<List<Category>> categories;
    private Product product;
    private MutableLiveData<List<Product>> products;

    public HomeViewModel(Activity activity) {
        this_activity = activity;
        category = new Category();
        categories = category.getAllCategories();
        product = new Product();
        products = product.getAllProducts();//TODO get by chunks
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<List<Product>> getProductsCategory(String categoryID){
        return product.getProductsCategory(categoryID);
    }
}