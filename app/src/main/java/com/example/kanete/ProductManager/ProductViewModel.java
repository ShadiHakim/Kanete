package com.example.kanete.ProductManager;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.CartItem;
import com.example.kanete.Models.Product;
import com.example.kanete.Models.User;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private Activity this_activity;
    private String type;
    private Product product;
    private MutableLiveData<List<String>> images;

    public ProductViewModel(Activity activity) {
        this.this_activity = activity;
        this.type = activity.getIntent().getStringExtra("userType");
        Toast.makeText(activity, type, Toast.LENGTH_SHORT).show();
        this.product = (Product) activity.getIntent().getSerializableExtra("selectedProduct"); // TODO check if exists
        this.images = product.getProductImages();
    }

    public LiveData<List<String>> getImages() {
        return this.images;
    }

    public Product getProduct() {
        return product;
    }

    public boolean isCustomer(){
        return type.contains("Customer");
    }

    public LiveData<Boolean> addToCart(int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct_ID(product.getID());
        cartItem.setCustomer_UID(new User().getUID());
        cartItem.setQuantity(quantity);
        return cartItem.addToCart();
    }
}
