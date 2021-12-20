package com.example.kanete.Customer.Address;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.kanete.Adapters.AddressRecyclerViewAdapter;
import com.example.kanete.Models.Address;
import com.example.kanete.R;

import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressRecyclerViewAdapter.AddressClickListener{

    private AddressViewModel addressViewModel;
    private AddressRecyclerViewAdapter addressRecyclerViewAdapter;

    private CardView cardViewAddAddress;
    private RecyclerView recyclerViewAddress;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        init();
    }

    private void init() {
        addressViewModel = new AddressViewModel();
        cardViewAddAddress = findViewById(R.id.cardViewAddAddress);
        recyclerViewAddress = findViewById(R.id.recyclerViewAddress);
        dialog = new Dialog(AddressActivity.this);

        setup_recyclerViewAddress();
        cardViewAddAddress.setOnClickListener(addAddresslistener);
    }

    public void setup_recyclerViewAddress(){
        addressViewModel.getMyAddresses().observeForever(new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                if (recyclerViewAddress.getAdapter() == null) {
                    recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    addressRecyclerViewAdapter = new AddressRecyclerViewAdapter(getApplicationContext(), addresses);
                    addressRecyclerViewAdapter.setClickListener(AddressActivity.this);
                    recyclerViewAddress.setAdapter(addressRecyclerViewAdapter);
                }
                else {
                    addressRecyclerViewAdapter.setAddresses(addresses);
                    addressRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void onAddressClick(View view, int position) {
        Address address = addressRecyclerViewAdapter.getAddress(position);
        addressViewModel.changeDefault(address).observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean flag) {
                List<Address> updated_address = addressViewModel.updatedDefaults(addressRecyclerViewAdapter.getAddresses(), address);
                addressRecyclerViewAdapter.setAddresses(updated_address);
                addressRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    View.OnClickListener addAddresslistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.setContentView(R.layout.dialog_add_address);

            EditText editTextTextPersonNameAddress = dialog.findViewById(R.id.editTextTextPersonNameAddress);
            EditText editTextPhoneAddress = dialog.findViewById(R.id.editTextPhoneAddress);
            EditText editTextCountryAddress = dialog.findViewById(R.id.editTextCountryAddress);
            EditText editTextCityAddress = dialog.findViewById(R.id.editTextCityAddress);
            EditText editTextStreetAddress = dialog.findViewById(R.id.editTextStreetAddress);
            EditText editTextNumberAddress = dialog.findViewById(R.id.editTextNumberAddress);
            EditText editTextZipCodeAddress = dialog.findViewById(R.id.editTextZipCodeAddress);
            CheckBox checkBoxIsDefaultAddress = dialog.findViewById(R.id.checkBoxIsDefaultAddress);
            Button buttonAddAddress = dialog.findViewById(R.id.buttonAddAddress);

            buttonAddAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Address address = new Address();
                    address.setContact_name(editTextTextPersonNameAddress.getText().toString());
                    address.setPhone_number(editTextPhoneAddress.getText().toString());
                    address.setCountry(editTextCountryAddress.getText().toString());
                    address.setCity(editTextCityAddress.getText().toString());
                    address.setStreet(editTextStreetAddress.getText().toString());
                    address.setNumber(editTextNumberAddress.getText().toString());
                    address.setZipcode(Integer.parseInt(editTextZipCodeAddress.getText().toString()));
                    address.setDefault_adr(checkBoxIsDefaultAddress.isChecked());

                    // TODO add checks

                    addressViewModel.addAddress(address).observeForever(new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean flag) {
                            if (flag)
                                dialog.dismiss();
                        }
                    });
                }
            });

            dialog.show();
        }
    };
}