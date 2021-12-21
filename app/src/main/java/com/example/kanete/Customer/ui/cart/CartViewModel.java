package com.example.kanete.Customer.ui.cart;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.Address;
import com.example.kanete.Models.CartItem;
import com.example.kanete.Models.Order;
import com.example.kanete.Models.Payment;
import com.example.kanete.Models.Product;
import com.example.kanete.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartViewModel extends ViewModel {

    private CartFragment cartFragment;
    private CartItem tcartItem;
    private MutableLiveData<List<CartItem>> cartItems;
    private MutableLiveData<List<Product>> cartProducts;
    private MutableLiveData<List<CartItem>> selectedCartItems;

    private MutableLiveData<Address> myAddress;
    private MutableLiveData<Payment> myPayment;

    public CartViewModel(CartFragment cartFragment) {
        this.cartFragment = cartFragment;
        tcartItem = new CartItem();
        cartItems = new MutableLiveData<>();
        cartProducts = new MutableLiveData<>();
        selectedCartItems = new MutableLiveData<>(new ArrayList<>());

        myAddress = new MutableLiveData<>();
        myPayment = new MutableLiveData<>();

        initCart();
    }

    public void initCart(){
        tcartItem.getCart(new User().getUID()).observeForever(new Observer<List<CartItem>>() {
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

    public MutableLiveData<Address> getMyAddress() {
        return myAddress;
    }

    public MutableLiveData<Payment> getMyPayment() {
        return myPayment;
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
        for (CartItem cartItem :
                selectedCartItems.getValue()) {
            cartItem.removeFormCart();
        }
    }

    public LiveData<Boolean> getDefaultDetails(){
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        Address.getDefault().observe(cartFragment.getViewLifecycleOwner(), new Observer<Address>() {
            @Override
            public void onChanged(Address address) {
                myAddress.postValue(address);
                Payment.getDefault().observe(cartFragment.getViewLifecycleOwner(), new Observer<Payment>() {
                    @Override
                    public void onChanged(Payment payment) {
                        myPayment.postValue(payment);
                        flag.postValue(true);
                    }
                });
            }
        });
        return flag;
    }

    public void buyAll() {
        for (CartItem cartItem :
                cartItems.getValue()) {
            cartItemToOrder(cartItem).createOrder(cartItem);
        }
    }

    public void buySelected() {
        for (CartItem cartItem :
                selectedCartItems.getValue()) {
            cartItemToOrder(cartItem).createOrder(cartItem);
        }
    }

    public Order cartItemToOrder(CartItem cartItem){
        Product product = cartProducts.getValue().stream()
                .filter(item -> item.getID()
                        .equals(cartItem.getProduct_ID()))
                .collect(Collectors.toList()).get(0);
        Order order = new Order();
        order.setStore_UID(product.getStore_UID());
        order.setCustomer_UID(cartItem.getCustomer_UID());
        order.setProduct_ID(cartItem.getProduct_ID());
        order.setAddress_ID(myAddress.getValue().getID());
        order.setPayment_ID(myPayment.getValue().getID());
        order.setQuantity(cartItem.getQuantity());
        order.setStatus("Not handled");
        order.setComplete(false);
        return order;
    }
}