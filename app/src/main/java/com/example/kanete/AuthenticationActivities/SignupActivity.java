package com.example.kanete.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kanete.Models.Customer;
import com.example.kanete.Models.Store;
import com.example.kanete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    AuthenticationViewModel authenticationViewModel;
    RadioGroup radioGroupType;

    // Customer
    private NestedScrollView NestedScrollCustomer;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private RadioGroup RadioGroupGender;
    private EditText editTextSignupEmailCustomer;
    private EditText editTextSignupPasswordCustomer;
    private EditText editTextSignupRePasswordCustomer;

    // Store
    NestedScrollView NestedScrollStore;
    private EditText editTextStoreName;
    private EditText editTextAddress;
    private EditText editTextPhone;
    private EditText editTextSignupEmailStore;
    private EditText editTextSignupPasswordStore;
    private EditText editTextRePasswordStore;
    private EditText editTextFacebook;
    private EditText editTextInstagram;
    private EditText editTextWebsite;

    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
    }

    public void init(){
        authenticationViewModel = new AuthenticationViewModel(this);

        radioGroupType = findViewById(R.id.radioGroupType);
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

        // Customer
        NestedScrollCustomer = findViewById(R.id.NestedScrollCustomer);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        RadioGroupGender = findViewById(R.id.RadioGroupGender);
        editTextSignupEmailCustomer = findViewById(R.id.editTextSignupEmailCustomer);
        editTextSignupPasswordCustomer = findViewById(R.id.editTextSignupPasswordCustomer);
        editTextSignupRePasswordCustomer = findViewById(R.id.editTextSignupRePasswordCustomer);

        // Store
        NestedScrollStore = findViewById(R.id.NestedScrollStore);
        editTextStoreName = findViewById(R.id.editTextStoreName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextSignupEmailStore = findViewById(R.id.editTextSignupEmailStore);
        editTextSignupPasswordStore = findViewById(R.id.editTextSignupPasswordStore);
        editTextRePasswordStore = findViewById(R.id.editTextRePasswordStore);
        editTextFacebook = findViewById(R.id.editTextFacebook);
        editTextInstagram = findViewById(R.id.editTextInstagram);
        editTextWebsite = findViewById(R.id.editTextWebsite);

        buttonSignup = findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup(){
        boolean flag;
        int type = radioGroupType.getCheckedRadioButtonId();
        if (type == R.id.radioButtonCustomer){
            String firstname = editTextFirstName.getText().toString();
            String lastname = editTextLastName.getText().toString();
            int gender_id = RadioGroupGender.getCheckedRadioButtonId();
            String gender = "";
            String email = editTextSignupEmailCustomer.getText().toString();
            String password = editTextSignupPasswordCustomer.getText().toString();
            String repassword = editTextSignupRePasswordCustomer.getText().toString();

            flag = AuthenticationUtils.checkFirstName(firstname, editTextFirstName);
            flag &= AuthenticationUtils.checkLastName(lastname, editTextLastName);
            gender = AuthenticationUtils.getGender(gender_id);
            flag &= AuthenticationUtils.checkEmail(email, editTextSignupEmailCustomer);
            flag &= AuthenticationUtils.checkPassword(password, editTextSignupPasswordCustomer);
            flag &= AuthenticationUtils.checkRePassword(password, repassword, editTextSignupRePasswordCustomer);

            if (flag){
                Customer customer = new Customer();
                customer.setFirst_Name(firstname);
                customer.setLast_Name(lastname);
                customer.setGender(gender);

                // TODO move all Strings to strings.xml
                authenticationViewModel.signup_auth("type_customer", email, password, customer, null);
            }

        }
        else {
            String name = editTextStoreName.getText().toString();
            String address = editTextAddress.getText().toString();
            String phone = editTextPhone.getText().toString();
            String email = editTextSignupEmailStore.getText().toString();
            String password = editTextSignupPasswordStore.getText().toString();
            String repassword = editTextRePasswordStore.getText().toString();
            String facebook = editTextFacebook.getText().toString();
            String instagram = editTextInstagram.getText().toString();
            String website = editTextWebsite.getText().toString();

            flag = AuthenticationUtils.checkName(name, editTextStoreName);
            flag &= AuthenticationUtils.checkAddress(address, editTextAddress);
            flag &= AuthenticationUtils.checkPhone(phone, editTextPhone);
            flag &= AuthenticationUtils.checkEmail(email, editTextSignupEmailStore);
            flag &= AuthenticationUtils.checkPassword(password, editTextSignupPasswordStore);
            flag &= AuthenticationUtils.checkRePassword(password, repassword, editTextRePasswordStore);
            flag &= AuthenticationUtils.checkFacebook(facebook, editTextFacebook);
            flag &= AuthenticationUtils.checkInstagram(instagram, editTextInstagram);
            flag &= AuthenticationUtils.checkWebsite(website, editTextWebsite);


            if (flag) {
                Store store = new Store();
                store.setName(name);
                store.setAddress(address);
                store.setPhone(phone);
                store.setFacebook(facebook);
                store.setInstagram(instagram);
                store.setWebsite(website);

                authenticationViewModel.signup_auth("type_store", email, password, null, store);
            }

        }

    }
}