package com.example.kanete.Customer.Address;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.Address;
import com.example.kanete.Models.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class AddressViewModel extends ViewModel {

    private Address default_address;
    private List<Address> addressList;

    public AddressViewModel() {
        default_address = new Address();
    }

    public LiveData<Boolean> addAddress(Address address) {
        return address.addAddress(default_address);
    }

    public LiveData<List<Address>> getMyAddresses(){
        return default_address.getMyAddress();
    }

    public LiveData<Boolean> changeDefault(Address new_default_address) {
        return new_default_address.updateDefault(default_address);
    }

    public List<Address> updatedDefaults(List<Address> addresses, Address new_default_address){
        addresses.stream()
                .filter(item -> item.getID()
                        .equals(default_address.getID()))
                .peek(item -> item.setDefault_adr(false));
        addresses.stream()
                .filter(item -> item.getID()
                        .equals(new_default_address.getID()))
                .peek(item -> item.setDefault_adr(true));
        return addresses;
    }
}
