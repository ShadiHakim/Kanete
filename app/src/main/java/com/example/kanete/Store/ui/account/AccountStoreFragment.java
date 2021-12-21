package com.example.kanete.Store.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kanete.OrderManager.OrdersListActivity;
import com.example.kanete.databinding.AccountStoreFragmentBinding;
import com.example.kanete.helper.Utils;

public class AccountStoreFragment extends Fragment {

    private AccountStoreViewModel accountStoreViewModel;
    private AccountStoreFragmentBinding binding;

    private Button buttonStoreOrders;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AccountStoreFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    public void init(){
        accountStoreViewModel = new AccountStoreViewModel(this.getActivity());
        buttonStoreOrders = binding.buttonStoreOrders;

        buttonStoreOrders.setOnClickListener(storeOrderslistener);

        final Button button_signout = binding.AccountStoreFragmentSignoutButton;
        button_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountStoreViewModel.sign_out();
            }
        });
    }

    View.OnClickListener storeOrderslistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.goTo_withExtra(getActivity(), OrdersListActivity.class, "Store");
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}