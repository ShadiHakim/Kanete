package com.example.kanete.Store.ui.account;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kanete.R;
import com.example.kanete.Store.ui.addproducts.AddProductViewModel;
import com.example.kanete.databinding.AccountStoreFragmentBinding;
import com.example.kanete.databinding.AddProductFragmentBinding;

public class AccountStoreFragment extends Fragment {

    private AccountStoreViewModel accountStoreViewModel;
    private AccountStoreFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountStoreViewModel =
                new ViewModelProvider(this).get(AccountStoreViewModel.class);

        binding = AccountStoreFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAccountstore;
        accountStoreViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}