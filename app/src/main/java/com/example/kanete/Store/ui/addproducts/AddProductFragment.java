package com.example.kanete.Store.ui.addproducts;

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
import com.example.kanete.Store.ui.myproducts.MyProductsViewModel;
import com.example.kanete.databinding.AddProductFragmentBinding;
import com.example.kanete.databinding.MyProductsFragmentBinding;

public class AddProductFragment extends Fragment {

    private AddProductViewModel addProductViewModel;
    private AddProductFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = AddProductFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    private void init() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}