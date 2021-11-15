package com.example.kanete.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kanete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextSignupEmail;
    private EditText editTextSignupPassword;
    private EditText editTextRePassword;

    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        editTextSignupEmail = findViewById(R.id.editTextSignupEmail);
        editTextSignupPassword = findViewById(R.id.editTextSignupPassword);
        editTextRePassword = findViewById(R.id.editTextRePassword);

        buttonSignup = findViewById(R.id.buttonSignup);

        NestedScrollView NestedScrollCustomer = findViewById(R.id.NestedScrollCustomer);
        NestedScrollView NestedScrollStore = findViewById(R.id.NestedScrollStore);

        RadioGroup radioGroupType = findViewById(R.id.radioGroupType);
        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.radioButtonCustomer == checkedId){
                    NestedScrollCustomer.setVisibility(View.VISIBLE);
                    NestedScrollStore.setVisibility(View.GONE);
                }
                if (R.id.radioButtonStore == checkedId){
                    NestedScrollCustomer.setVisibility(View.GONE);
                    NestedScrollStore.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup(){
        String email = editTextSignupEmail.getText().toString();
        String password = editTextSignupPassword.getText().toString();
        String repassword = editTextRePassword.getText().toString();

        if (password.length() < 6 && !password.equals(repassword)){
            Toast.makeText(SignupActivity.this, "Check your password!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}