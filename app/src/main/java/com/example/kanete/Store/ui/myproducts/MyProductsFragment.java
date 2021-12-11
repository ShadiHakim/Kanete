package com.example.kanete.Store.ui.myproducts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kanete.Adapters.ProductRecyclerViewAdapter;
import com.example.kanete.Models.Product;
import com.example.kanete.databinding.MyProductsFragmentBinding;

import java.util.List;

public class MyProductsFragment extends Fragment {

    private MyProductsViewModel myProductsViewModel;
    private MyProductsFragmentBinding binding;

    private ProductRecyclerViewAdapter productRecyclerViewAdapter;
    private RecyclerView recyclerViewMyProducts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = MyProductsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    public void init(){
        myProductsViewModel = new MyProductsViewModel(getActivity());
        recyclerViewMyProducts = binding.recyclerViewMyProducts;

        setup_recyclerViewMyProducts();
    }

    public void setup_recyclerViewMyProducts(){
        myProductsViewModel.getMyProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (recyclerViewMyProducts.getAdapter() == null) {
                    recyclerViewMyProducts.setLayoutManager(new LinearLayoutManager(getContext()));
                    productRecyclerViewAdapter = new ProductRecyclerViewAdapter(getContext(), products);
//                    productRecyclerViewAdapter.setClickListener(this);
                    recyclerViewMyProducts.setAdapter(productRecyclerViewAdapter);
                }
                else {
                    productRecyclerViewAdapter.setProducts(products);
                    productRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}