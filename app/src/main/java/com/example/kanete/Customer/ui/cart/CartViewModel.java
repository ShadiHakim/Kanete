package com.example.kanete.Customer.ui.cart;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.CartItem;
import com.example.kanete.Models.Product;
import com.example.kanete.Models.User;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {

    private Activity this_activity;
    private CartItem cartItem;
    private MutableLiveData<List<CartItem>> cartItems;
    private MutableLiveData<List<Product>> cartProducts;
    private MutableLiveData<List<CartItem>> selectedCartItems;

    public CartViewModel(FragmentActivity activity) {
        this_activity = activity;
        cartItem = new CartItem();
        cartItems = new MutableLiveData<>();
        cartProducts = new MutableLiveData<>();
        selectedCartItems = new MutableLiveData<>(new ArrayList<>());
        initCart();
    }

    public void initCart(){
        cartItem.getCart(new User().getUID()).observeForever(new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cart) {
                cartItems.postValue(cart);
                Product.getProductsOfCart(cart).observeForever(new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        if (products.size() == cart.size()){
                            cartProducts.postValue(products);
                        }
                    }
                });
            }
        });
    }

    public LiveData<List<Product>> getCart() {
        return cartProducts;
    }

    public MutableLiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    public void addSelected(CartItem selectedCartItem){
        List<CartItem> curSelected = selectedCartItems.getValue();
        curSelected.add(selectedCartItem);
        selectedCartItems.postValue(curSelected);
    }

    public void removeSelected(CartItem remSelectedProduct){
        List<CartItem> curSelected = selectedCartItems.getValue();
        curSelected.remove(remSelectedProduct);
        selectedCartItems.postValue(curSelected);
    }

    public void selectAll(){
        selectedCartItems.postValue(cartItems.getValue());
    }

    public void removeAllSelected(){
        selectedCartItems.postValue(new ArrayList<>());
    }

    public void removeSelectedItemsFormCart(){
        cartItem.removeFormCart(selectedCartItems.getValue());
    }

    public void buyAll() {
        // TODO
    }

    public void buySelected() {
        // TODO
    }
}