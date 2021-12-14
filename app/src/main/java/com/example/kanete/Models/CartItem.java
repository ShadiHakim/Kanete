package com.example.kanete.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartItem {
    private String ID;
    private String customer_UID;
    private String product_ID;
    private int quantity;

    @Exclude
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCustomer_UID() {
        return customer_UID;
    }

    public void setCustomer_UID(String customer_UID) {
        this.customer_UID = customer_UID;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public LiveData<Boolean> addToCart(){
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("CartItems")
                .add(this)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        flag.postValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        flag.postValue(false);
                    }
                });
        return flag;
    }

    @Exclude
    public LiveData<List<CartItem>> getCart(String uid) {
        MutableLiveData<List<CartItem>> cartProducts = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("CartItems")
                .whereEqualTo("customer_UID", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<CartItem> cartItems = new ArrayList<>();
                        if (value != null) { // TODO fix
                            for (DocumentSnapshot documentSnapshot :
                                    value.getDocuments()) {
                                CartItem cartItem = documentSnapshot.toObject(CartItem.class);
                                cartItem.setID(documentSnapshot.getId());
                                cartItems.add(cartItem);
                            }
                            cartProducts.postValue(cartItems);
                        }
                    }
                });
        return cartProducts;
    }
}
