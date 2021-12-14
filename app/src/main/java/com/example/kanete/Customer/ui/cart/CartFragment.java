package com.example.kanete.Customer.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Adapters.CartRecyclerViewAdapter;
import com.example.kanete.Adapters.ProductRecyclerViewAdapter;
import com.example.kanete.Models.CartItem;
import com.example.kanete.Models.Product;
import com.example.kanete.Store.ui.myproducts.MyProductsFragment;
import com.example.kanete.databinding.FragmentCartBinding;

import java.util.List;

public class CartFragment extends Fragment implements CartRecyclerViewAdapter.ProductClickListener{

    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;

    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    private RecyclerView recyclerViewCart;

    private Button buttonBuy;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    private void init() {
        cartViewModel = new CartViewModel(getActivity());
        recyclerViewCart = binding.recyclerViewCart;
        buttonBuy = binding.buttonBuy;

        setup_recyclerViewCart();
    }

    public void setup_recyclerViewCart(){
        cartViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                List<CartItem> cartItems = cartViewModel.getCartItems().getValue();
                if (recyclerViewCart.getAdapter() == null) {
                    recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
                    cartRecyclerViewAdapter = new CartRecyclerViewAdapter(getContext(), products, cartItems);
                    cartRecyclerViewAdapter.setClickListener(CartFragment.this);
                    recyclerViewCart.setAdapter(cartRecyclerViewAdapter);
                }
                else {
                    cartRecyclerViewAdapter.setProducts(products);
                    cartRecyclerViewAdapter.setCartItems(cartItems);
                    cartRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onProductClick(View view, int position) {
        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onProductLongClick(View view, int position) {
        Toast.makeText(getContext(), "long click ", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void checkBoxClick(View view, int position) {
        Toast.makeText(getContext(), "check box " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}