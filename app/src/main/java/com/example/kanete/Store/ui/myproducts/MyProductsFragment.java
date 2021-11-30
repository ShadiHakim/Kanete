package com.example.kanete.Store.ui.myproducts;

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
import com.example.kanete.databinding.AddProductFragmentBinding;
import com.example.kanete.databinding.MyProductsFragmentBinding;

public class MyProductsFragment extends Fragment {

    private MyProductsViewModel myProductsViewModel;
    private MyProductsFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myProductsViewModel =
                new ViewModelProvider(this).get(MyProductsViewModel.class);

        binding = MyProductsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMyProducts;
        myProductsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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