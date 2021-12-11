package com.example.kanete.Customer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Adapters.CategoryRecyclerViewAdapter;
import com.example.kanete.Adapters.ProductRecyclerViewAdapter;
import com.example.kanete.Models.Category;
import com.example.kanete.Models.Product;
import com.example.kanete.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    private RecyclerView recyclerViewCategory;

    private ProductRecyclerViewAdapter productRecyclerViewAdapter;
    private RecyclerView recyclerViewProducts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    private void init() {
        homeViewModel = new HomeViewModel(getActivity());
        recyclerViewCategory = binding.recyclerViewCategory;
        recyclerViewProducts = binding.recyclerViewProducts;

        setup_recyclerViewCategory();
        setup_recyclerViewProducts();
    }

    public void setup_recyclerViewCategory(){
        homeViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(getContext(), categories);
//                categoryRecyclerViewAdapter.setClickListener(this);
                recyclerViewCategory.setAdapter(categoryRecyclerViewAdapter);
            }
        });
    }

    public void setup_recyclerViewProducts(){
        homeViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
                productRecyclerViewAdapter = new ProductRecyclerViewAdapter(getContext(), products);
//                productRecyclerViewAdapter.setClickListener(this);
                recyclerViewProducts.setAdapter(productRecyclerViewAdapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}