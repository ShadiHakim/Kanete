package com.example.kanete.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String ID;
    private String store_UID;
    private String customer_UID;
    private String product_ID;
    private String address_ID;
    private String Payment_ID;
    private int quantity;
    private String status;
    private boolean complete;

    private LiveData<Product> live_product;
    private LiveData<Address> live_address;
    private LiveData<Payment> live_payment;

    @Exclude
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStore_UID() {
        return store_UID;
    }

    public void setStore_UID(String store_UID) {
        this.store_UID = store_UID;
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

    public String getAddress_ID() {
        return address_ID;
    }

    public void setAddress_ID(String address_ID) {
        this.address_ID = address_ID;
    }

    public String getPayment_ID() {
        return Payment_ID;
    }

    public void setPayment_ID(String payment_ID) {
        Payment_ID = payment_ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }


    @Exclude
    public LiveData<Product> getLive_product() {
        return live_product;
    }

    public void setLive_product(LiveData<Product> live_product) {
        this.live_product = live_product;
    }

    @Exclude
    public LiveData<Address> getLive_address() {
        return live_address;
    }

    public void setLive_address(LiveData<Address> live_address) {
        this.live_address = live_address;
    }

    @Exclude
    public LiveData<Payment> getLive_payment() {
        return live_payment;
    }

    public void setLive_payment(LiveData<Payment> live_payment) {
        this.live_payment = live_payment;
    }

    @Exclude
    public LiveData<Boolean> createOrder(CartItem cartItem){
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Orders")
                .add(this)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        flag.postValue(true);
                        cartItem.removeFormCart();
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
    public static LiveData<List<Order>> getMyOrders(User.types type){
        MutableLiveData<List<Order>> myOrders = new MutableLiveData<>(new ArrayList<>());
        if (type == User.types.Store){
            FirebaseFirestore.getInstance().collection("Orders")
                    .whereEqualTo("store_UID", new User().getUID())
                    .whereEqualTo("complete", false)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            List<Order> orderList = new ArrayList<>();
                            if (value != null) { // TODO fix
                                for (DocumentSnapshot documentSnapshot :
                                        value.getDocuments()) {
                                    Order order = documentSnapshot.toObject(Order.class);
                                    order.setID(documentSnapshot.getId());

                                    order.setLive_product(Product.getProductByID(order.product_ID));
                                    order.setLive_address(Address.getAddressByID(order.address_ID));
                                    order.setLive_payment(Payment.getPaymentByID(order.Payment_ID));

                                    orderList.add(order);
                                }
                                myOrders.postValue(orderList);
                            }
                        }
                    });
        }
        else {
            FirebaseFirestore.getInstance().collection("Orders")
                    .whereEqualTo("customer_UID", new User().getUID())
                    .whereEqualTo("complete", false)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            List<Order> orderList = new ArrayList<>();
                            if (value != null) { // TODO fix
                                for (DocumentSnapshot documentSnapshot :
                                        value.getDocuments()) {
                                    Order order = documentSnapshot.toObject(Order.class);
                                    order.setID(documentSnapshot.getId());

                                    order.setLive_product(Product.getProductByID(order.product_ID));
                                    order.setLive_address(Address.getAddressByID(order.address_ID));
                                    order.setLive_payment(Payment.getPaymentByID(order.Payment_ID));

                                    orderList.add(order);
                                }
                                myOrders.postValue(orderList);
                            }
                        }
                    });
        }
        return myOrders;
    }

    @Exclude
    public LiveData<Boolean> updateStatus() {
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Orders")
                .document(getID())
                .update("status", getStatus())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        flag.postValue(true);
                    }
                });
        return flag;
    }

    @Exclude
    public LiveData<Boolean> updateComplete() {
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Orders")
                .document(getID())
                .update("complete", isComplete())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        flag.postValue(true);
                    }
                });
        return flag;
    }
}
