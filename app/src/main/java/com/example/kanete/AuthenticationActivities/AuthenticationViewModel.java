package com.example.kanete.AuthenticationActivities;

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
import com.example.kanete.Models.UserType;
import com.example.kanete.Store.StoreMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public String getUID(){
        return mAuth.getValue().getCurrentUser().getUid();
    }

    public void signup_auth(UserType.types type, String email, String password, Customer customer, Store store){
        mAuth.getValue().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this_activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (type.equals(UserType.types.Customer)){
                                type_signup(UserType.types.Customer);
                                customerSignup_database(customer);
                            }
                            else {
                                type_signup(UserType.types.Store);
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
        db.getValue().collection("Customers")
                .document(getUID())
                .set(customer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        goTo(CustomerMainActivity.class);
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
        db.getValue().collection("Stores")
                .document(getUID())
                .set(store)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        goTo(StoreMainActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(this_activity, "failed FirebaseFirestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void type_signup(UserType.types type){
        UserType userType = new UserType(type);
        db.getValue().collection("UserType")
                .document(getUID())
                .set(userType)
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
        mAuth.getValue().signInWithEmailAndPassword(email, password)
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
        db.getValue().collection("UserType")
                .document(getUID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserType userType = documentSnapshot.toObject(UserType.class);
                        if (userType.getType().equals(UserType.types.Customer)){
                            goTo(CustomerMainActivity.class);
                        }
                        else {
                            goTo(StoreMainActivity.class);
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

    public void goTo(final Class<?> activity) {
        Intent i = new Intent(this_activity, activity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this_activity.startActivity(i);
        this_activity.finish();
    }
}
