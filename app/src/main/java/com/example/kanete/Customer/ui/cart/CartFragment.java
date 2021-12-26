package com.example.kanete.Customer.ui.cart;

import static com.example.kanete.helper.Utils.goTo_withProduct;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Adapters.CartRecyclerViewAdapter;
import com.example.kanete.Adapters.ProductRecyclerViewAdapter;
import com.example.kanete.Models.Address;
import com.example.kanete.Models.CartItem;
import com.example.kanete.Models.Payment;
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
        cartViewModel = new CartViewModel(this);
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
            resetCartViews();
        }
    };

    View.OnClickListener buyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogDetailsCheck().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean flag) {
                    if (flag){
                        if (cartRecyclerViewAdapter.isFlagVisible()){
                            cartViewModel.buySelected();
                        }
                        else {
                            cartViewModel.buyAll();
                        }
                    }
                    resetCartViews();
                }
            });
        }
    };

    public LiveData<Boolean> dialogDetailsCheck(){
        MutableLiveData<Boolean> flag = new MutableLiveData<>();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        flag.postValue(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        flag.postValue(false);
                        resetCartViews();
                        break;
                }
            }
        };

        cartViewModel.getDefaultDetails().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean flag) {
                if (flag){
                    Address defAddr = cartViewModel.getMyAddress().getValue();
                    Payment defPay = cartViewModel.getMyPayment().getValue();
                    if (defAddr != null && defPay != null){
                        String message = String.format("Your default address details:\n%s\n%s\n%s %s\n%s %s\n%s\n\nYour default payment details:\n%s\n%s\n%s\n%s",
                                defAddr.getContact_name(), defAddr.getPhone_number(), defAddr.getStreet(), defAddr.getNumber(), defAddr.getCity(), defAddr.getCountry(), defAddr.getZipcode(),
                                defPay.getName_holder(), defPay.getCredit_card(), defPay.getExpiration_date(), defPay.getCvv());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Are you sure you want to make the purchase?").setMessage(message).setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Please make sure you have default details", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return flag;
    }

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
//        Product product = cartRecyclerViewAdapter.getProduct(position);
        if (((CheckBox)view).isChecked()){
            cartViewModel.addSelected((CartItem) view.getTag());
        }
        else {
            cartViewModel.removeSelected((CartItem) view.getTag());
        }
    }

    public void resetCartViews(){
        constraintLayoutCartEdit.setVisibility(View.GONE);
        buttonBuy.setText(R.string.CartFragment_Buy_All);
        cartRecyclerViewAdapter.setFlagVisible(false);
        cartRecyclerViewAdapter.setAllChecked(false);
        checkBoxSelectAll.setChecked(false);
        cartViewModel.removeAllSelected();
        cartRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}