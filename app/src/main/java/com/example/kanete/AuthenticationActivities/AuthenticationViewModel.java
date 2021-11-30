package com.example.kanete.Activities;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Customer.CustomerMainActivity;
import com.example.kanete.Models.Customer;
import com.example.kanete.Models.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

// TODO move all Strings to strings.xml
public class AuthenticationViewModel extends ViewModel {

    private final Activity this_activity;
    private MutableLiveData<FirebaseAuth> mAuth;
    private MutableLiveData<FirebaseFirestore> db;

    public AuthenticationViewModel(Activity activity) {
        this_activity = activity;
        mAuth = new MutableLiveData<>();
        mAuth.setValue(FirebaseAuth.getInstance());
        db = new MutableLiveData<>();
        db.setValue(FirebaseFirestore.getInstance());
    }

    public LiveData<FirebaseAuth> getAuth() {
        return mAuth;
    }

    public LiveData<FirebaseFirestore> getDB() {
        return db;
    }

    public void signup_auth(String type, String email, String password, Customer customer, Store store){

        mAuth.getValue().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this_activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (type.equals("type_customer")){
                                customerSignup_database(customer);
                            }
                            else {
                                storeSignup_database(store);
                            }
                        } else {
                            Toast.makeText(this_activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void customerSignup_database(Customer customer){
        FirebaseUser user = mAuth.getValue().getCurrentUser();
        db.getValue().collection("Customers")
                .document(user.getUid())
                .set(customer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        this_activity.startActivity(new Intent(this_activity, CustomerMainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(this_activity, "failed FirebaseFirestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void storeSignup_database(Store store){
        FirebaseUser user = mAuth.getValue().getCurrentUser();
        db.getValue().collection("Store")
                .document(user.getUid())
                .set(store)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(this_activity, "Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(this_activity, "failed FirebaseFirestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
