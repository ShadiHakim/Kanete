package com.example.kanete.Customer.ui.cart;

import static com.example.kanete.helper.Utils.goTo_withProduct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Adapters.CartRecyclerViewAdapter;
import com.example.kanete.Adapters.ProductRecyclerViewAdapter;
import com.example.kanete.Models.CartItem;
import com.example.kanete.Models.Product;
import com.example.kanete.ProductManager.ProductViewActivity;
import com.example.kanete.R;
import com.example.kanete.Store.ui.myproducts.MyProductsFragment;
import com.example.kanete.databinding.FragmentCartBinding;

import java.util.List;

public class CartFragment extends Fragment implements CartRecyclerViewAdapter.ProductClickListener{

    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;

    private ConstraintLayout constraintLayoutCartEdit;
    private CheckBox checkBoxSelectAll;
    private ImageButton imageButtonDelete;
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
        constraintLayoutCartEdit = binding.constraintLayoutCartEdit;
        checkBoxSelectAll = binding.checkBoxSelectAll;
        imageButtonDelete = binding.imageButtonDelete;
        recyclerViewCart = binding.recyclerViewCart;
        buttonBuy = binding.buttonBuy;

        setup_recyclerViewCart();
        checkBoxSelectAll.setOnClickListener(checkBoxListener);
        imageButtonDelete.setOnClickListener(deleteListener);
        buttonBuy.setOnClickListener(buyListener);
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

    View.OnClickListener checkBoxListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox)v;
            cartRecyclerViewAdapter.setAllChecked(checkBox.isChecked());
            if (checkBox.isChecked())
                cartViewModel.selectAll();
            else
                cartViewModel.removeAllSelected();
            cartRecyclerViewAdapter.notifyDataSetChanged();
        }
    };

    View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cartViewModel.removeSelectedItemsFormCart();
            constraintLayoutCartEdit.setVisibility(View.GONE);
            cartRecyclerViewAdapter.setFlagVisible(false);
            cartRecyclerViewAdapter.setAllChecked(false);
            checkBoxSelectAll.setChecked(false);
            cartViewModel.removeAllSelected();
        }
    };

    View.OnClickListener buyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cartRecyclerViewAdapter.isFlagVisible()){
                cartViewModel.buySelected();
            }
            else {
                cartViewModel.buyAll();
            }
        }
    };

    @Override
    public void onProductClick(View view, int position) {
        Product product = cartRecyclerViewAdapter.getProduct(position);
        goTo_withProduct(getActivity(), ProductViewActivity.class, product);
    }

    @Override
    public boolean onProductLongClick(View view, int position) {
//        Product product = cartRecyclerViewAdapter.getProduct(position);
        if (!cartRecyclerViewAdapter.isFlagVisible()){
            constraintLayoutCartEdit.setVisibility(View.VISIBLE);
            buttonBuy.setText(R.string.CartFragment_Buy_Selected);
            cartRecyclerViewAdapter.setFlagVisible(true);
        }
        else {
            constraintLayoutCartEdit.setVisibility(View.GONE);
            buttonBuy.setText(R.string.CartFragment_Buy_All);
            cartRecyclerViewAdapter.setFlagVisible(false);
            cartRecyclerViewAdapter.setAllChecked(false);
            checkBoxSelectAll.setChecked(false);
            cartViewModel.removeAllSelected();
        }
        cartRecyclerViewAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void checkBoxClick(View view, int position) {
        Product product = cartRecyclerViewAdapter.getProduct(position);
        if (((CheckBox)view).isChecked()){
            cartViewModel.addSelected((CartItem) view.getTag());
        }
        else {
            cartViewModel.removeSelected((CartItem) view.getTag());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}