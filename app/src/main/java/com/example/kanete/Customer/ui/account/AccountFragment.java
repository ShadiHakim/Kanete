package com.example.kanete.Customer.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.kanete.Customer.Address.AddressActivity;
import com.example.kanete.Customer.Payment.PaymentActivity;
import com.example.kanete.OrderManager.OrdersListActivity;
import com.example.kanete.databinding.FragmentAccountBinding;
import com.example.kanete.helper.Utils;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;

    private Button buttonAddress;
    private Button buttonPayment;
    private Button buttonCustomerOrders;
    private Button AccountFragmentSignoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = new AccountViewModel(this.getActivity());

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    public void init(){
        buttonAddress = binding.buttonAddress;
        buttonPayment = binding.buttonPayment;
        buttonCustomerOrders = binding.buttonCustomerOrders;
        AccountFragmentSignoutButton = binding.AccountFragmentSignoutButton;

        buttonAddress.setOnClickListener(addresslistener);
        buttonPayment.setOnClickListener(paymentlistener);
        buttonCustomerOrders.setOnClickListener(customerOrderslistener);
        AccountFragmentSignoutButton.setOnClickListener(signOutlistener);
    }

    View.OnClickListener signOutlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            accountViewModel.sign_out();
        }
    };

    View.OnClickListener addresslistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Utils.goTo(getActivity(), AddressActivity.class);
        }
    };

    View.OnClickListener paymentlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Utils.goTo(getActivity(), PaymentActivity.class);
        }
    };

    View.OnClickListener customerOrderslistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Utils.goTo_withExtra(getActivity(), OrdersListActivity.class, "Customer");
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}