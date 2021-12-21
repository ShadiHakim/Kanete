package com.example.kanete.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

public class Payment {
    private String ID;
    private String customer_UID;
    private String credit_card;
    private String expiration_date;
    private String name_holder;
    private String cvv;
    private boolean default_pay;

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

    public String getCredit_card() {
        return credit_card;
    }

    public void setCredit_card(String credit_card) {
        this.credit_card = credit_card;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getName_holder() {
        return name_holder;
    }

    public void setName_holder(String name_holder) {
        this.name_holder = name_holder;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public boolean isDefault_pay() {
        return default_pay;
    }

    public void setDefault_pay(boolean default_pay) {
        this.default_pay = default_pay;
    }

    @Exclude
    public LiveData<Boolean> addPayment(Payment default_payment) {
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        this.setCustomer_UID(new User().getUID());
        FirebaseFirestore.getInstance().collection("Payments")
                .add(this)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if (default_payment.getID() != null && !default_payment.getID().equals(documentReference.getId()))
                            FirebaseFirestore.getInstance().collection("Payments")
                                    .document(default_payment.getID())
                                    .update("default_pay", false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            flag.postValue(true);
                                        }
                                    });
                        else
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
    public LiveData<List<Payment>> getMyPayment() {
        MutableLiveData<List<Payment>> myPayment = new MutableLiveData<>(new ArrayList<>());
        FirebaseFirestore.getInstance().collection("Payments")
                .whereEqualTo("customer_UID", new User().getUID())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<Payment> paymentList = new ArrayList<>();
                        if (value != null) { // TODO fix
                            for (DocumentSnapshot documentSnapshot :
                                    value.getDocuments()) {
                                Payment payment = documentSnapshot.toObject(Payment.class);
                                payment.setID(documentSnapshot.getId());
                                paymentList.add(payment);
                                if (payment.isDefault_pay()){
                                    setID(payment.ID);
                                    setDefault_pay(true);
                                }
                            }
                            myPayment.postValue(paymentList);
                        }
                    }
                });
        return myPayment;
    }

    @Exclude
    public LiveData<Boolean> updateDefault(Payment default_payment) {
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        if (default_payment.getID() != null){
            FirebaseFirestore.getInstance().collection("Payments")
                    .document(default_payment.getID())
                    .update("default_pay",false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseFirestore.getInstance().collection("Payments")
                                    .document(getID())
                                    .update("default_pay", true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            flag.postValue(true);
                                        }
                                    });
                        }
                    });
        }
        else {
            FirebaseFirestore.getInstance().collection("Payments")
                    .document(getID())
                    .update("default_pay", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            flag.postValue(true);
                        }
                    });
        }
        return flag;
    }

    @Exclude
    public static LiveData<Payment> getDefault(){
        MutableLiveData<Payment> myDefaultPayment = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Payments")
                .whereEqualTo("customer_UID", new User().getUID())
                .whereEqualTo("default_pay", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            if (queryDocumentSnapshots.getDocuments().isEmpty()){
                                myDefaultPayment.postValue(null);
                            }
                            for (DocumentSnapshot documentSnapshot :
                                    queryDocumentSnapshots.getDocuments()) {
                                Payment defPay = documentSnapshot.toObject(Payment.class);
                                defPay.setID(documentSnapshot.getId());
                                myDefaultPayment.postValue(defPay);
                            }
                        }
                    }
                });
        return myDefaultPayment;
    }

    @Exclude
    public static LiveData<Payment> getPaymentByID(String ID){
        MutableLiveData<Payment> payment = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Payments")
                .document(ID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Payment res = documentSnapshot.toObject(Payment.class);
                        payment.postValue(res);
                    }
                });
        return payment;
    }
}
