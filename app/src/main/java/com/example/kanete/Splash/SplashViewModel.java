package com.example.kanete.Splash;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.AuthenticationActivities.LoginActivity;
import com.example.kanete.Customer.CustomerMainActivity;
import com.example.kanete.Models.UserType;
import com.example.kanete.Store.StoreMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashViewModel extends ViewModel {

    private final Activity this_activity;
    private MutableLiveData<FirebaseAuth> mAuth;
    private MutableLiveData<FirebaseFirestore> db;

    public SplashViewModel(Activity activity) {
        this_activity = activity;
        mAuth = new MutableLiveData<>();
        mAuth.setValue(FirebaseAuth.getInstance());
        db = new MutableLiveData<>();
        db.setValue(FirebaseFirestore.getInstance());
    }

    public String getUID(){
        return mAuth.getValue().getCurrentUser().getUid();
    }

    public void goToRelevantActivity(){
        if (mAuth.getValue().getCurrentUser() != null){
            type_login();
        }
        else {
            goTo(LoginActivity.class);
        }
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
