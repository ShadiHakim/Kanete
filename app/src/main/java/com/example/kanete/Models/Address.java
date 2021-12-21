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

public class Address {
    private String ID;
    private String customer_UID;
    private String contact_name;
    private String phone_number;
    private String country;
    private String city;
    private String street;
    private String number;
    private int zipcode;
    private boolean default_adr;

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

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public boolean isDefault_adr() {
        return default_adr;
    }

    public void setDefault_adr(boolean default_adr) {
        this.default_adr = default_adr;
    }

    @Exclude
    public LiveData<Boolean> addAddress(Address default_address){
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        this.setCustomer_UID(new User().getUID());
        FirebaseFirestore.getInstance().collection("Addresses")
                .add(this)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if (default_address.getID() != null && !default_address.getID().equals(documentReference.getId()))
                            FirebaseFirestore.getInstance().collection("Addresses")
                                    .document(default_address.getID())
                                    .update("default_adr", false)
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
    public LiveData<List<Address>> getMyAddress(){
        MutableLiveData<List<Address>> myAddress = new MutableLiveData<>(new ArrayList<>());
        FirebaseFirestore.getInstance().collection("Addresses")
                .whereEqualTo("customer_UID", new User().getUID())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<Address> addressList = new ArrayList<>();
                        if (value != null) { // TODO fix
                            for (DocumentSnapshot documentSnapshot :
                                    value.getDocuments()) {
                                Address address = documentSnapshot.toObject(Address.class);
                                address.setID(documentSnapshot.getId());
                                addressList.add(address);
                                if (address.isDefault_adr()){
                                    setID(address.ID);
                                    setDefault_adr(true);
                                }
                            }
                            myAddress.postValue(addressList);
                        }
                    }
                });
        return myAddress;
    }

    @Exclude
    public LiveData<Boolean> updateDefault(Address default_address) {
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        if (default_address.getID() != null){
            FirebaseFirestore.getInstance().collection("Addresses")
                    .document(default_address.getID())
                    .update("default_adr",false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseFirestore.getInstance().collection("Addresses")
                                    .document(getID())
                                    .update("default_adr", true)
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
            FirebaseFirestore.getInstance().collection("Addresses")
                    .document(getID())
                    .update("default_adr", true)
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
    public static LiveData<Address> getDefault(){
        MutableLiveData<Address> myDefaultAddress = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Addresses")
                .whereEqualTo("customer_UID", new User().getUID())
                .whereEqualTo("default_adr", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            if (queryDocumentSnapshots.getDocuments().isEmpty()){
                                myDefaultAddress.postValue(null);
                            }
                            for (DocumentSnapshot documentSnapshot :
                                    queryDocumentSnapshots.getDocuments()) {
                                Address defAdrr = documentSnapshot.toObject(Address.class);
                                defAdrr.setID(documentSnapshot.getId());
                                myDefaultAddress.postValue(defAdrr);
                            }
                        }
                    }
                });
        return myDefaultAddress;
    }
}
