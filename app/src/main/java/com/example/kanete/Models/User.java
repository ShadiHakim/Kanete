package com.example.kanete.Models;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kanete.AuthenticationActivities.LoginActivity;
import com.example.kanete.Customer.CustomerMainActivity;
import com.example.kanete.Store.StoreMainActivity;
import com.example.kanete.helper.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    public enum types {Customer, Store}

    private Activity this_activity;

    private types type;

    public User() {
    }

    public User(types type) {
        this.type = type;
    }

    public types getType() {
        return type;
    }

    public void setType(types type) {
        this.type = type;
    }

    @Exclude
    public Activity getThis_activity() {
        return this_activity;
    }

    public void setThis_activity(Activity this_activity) {
        this.this_activity = this_activity;
    }

    @Exclude
    public String getUID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Exclude
    public boolean isLoggedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public void signup_auth(User.types type, String email, String password, Customer customer, Store store){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this_activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (type.equals(User.types.Customer)){
                                type_signup(User.types.Customer);
                                customerSignup_database(customer);
                            }
                            else {
                                type_signup(User.types.Store);
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
        FirebaseFirestore.getInstance().collection("Customers")
                .document(getUID())
                .set(customer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Utils.goTo_emptyStack(this_activity, CustomerMainActivity.class);
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
        FirebaseFirestore.getInstance().collection("Stores")
                .document(getUID())
                .set(store)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Utils.goTo_emptyStack(this_activity, StoreMainActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(this_activity, "failed FirebaseFirestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void type_signup(User.types type){
        User user = new User(type);
        FirebaseFirestore.getInstance().collection("UserType")
                .document(getUID())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // onSuccess
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(this_activity, "failed FirebaseFirestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void login_auth(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        type_login();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(this_activity, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void type_login(){
        FirebaseFirestore.getInstance().collection("UserType")
                .document(getUID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user.getType().equals(User.types.Customer)){
                            Utils.goTo_emptyStack(this_activity, CustomerMainActivity.class);
                        }
                        else {
                            Utils.goTo_emptyStack(this_activity, StoreMainActivity.class);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(this_activity, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Utils.goTo_emptyStack(this_activity, LoginActivity.class);
    }
}
