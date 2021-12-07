package com.example.kanete.Store.ui.addproducts;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanete.Models.Category;
import com.example.kanete.Models.Product;
import com.example.kanete.R;
import com.example.kanete.databinding.AddProductFragmentBinding;
import com.kroegerama.imgpicker.BottomSheetImagePicker;

import java.util.ArrayList;
import java.util.List;

public class AddProductFragment extends Fragment implements BottomSheetImagePicker.OnImagesSelectedListener{

    private AddProductViewModel addProductViewModel;
    private AddProductFragmentBinding binding;

    private EditText editTextProductName;
    private ImageButton buttonProductImages;
    private TextView textViewImagesCounter;
    private EditText editTextProductPrice;
    private EditText editTextProductDescription;
    private EditText editTextProductQuantity;
    private Spinner spinnerProductCategory;
    private Button buttonAddProduct;
    private ProgressBar progressBarAddProduct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AddProductFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        return root;
    }

    private void init() {
        addProductViewModel = new AddProductViewModel(getActivity());
        editTextProductName = binding.editTextProductName;
        buttonProductImages = binding.buttonProductImages;
        textViewImagesCounter = binding.textViewImagesCounter;
        editTextProductPrice = binding.editTextProductPrice;
        editTextProductDescription = binding.editTextProductDescription;
        editTextProductQuantity = binding.editTextProductQuantity;
        spinnerProductCategory = binding.spinnerProductCategory;
        buttonAddProduct = binding.buttonAddProduct;
        progressBarAddProduct = binding.progressBarAddProduct;

        addProductViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, addProductViewModel.getCategories_name());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerProductCategory.setAdapter(adapter);
            }
        });

        buttonProductImages.setOnClickListener(imagesClick);

        buttonAddProduct.setOnClickListener(addProductClick);
    }

    View.OnClickListener imagesClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (addProductViewModel.allowPermission()){
                new BottomSheetImagePicker.Builder(getString(R.string.file_provider))
                        .multiSelect(1, 10)
                        .multiSelectTitles(
                                R.plurals.pick_multi,
                                R.plurals.pick_multi_more,
                                R.string.pick_multi_limit
                        )
                        .peekHeight(R.dimen.peekHeight)
                        .columnSize(R.dimen.columnSize)
                        .requestTag("multi")
                        .show(getChildFragmentManager(), null);
            }
        }
    };

    @Override
    public void onImagesSelected(List<? extends Uri> list, String s) {
        List<Uri> images = new ArrayList<>(list);
        addProductViewModel.setChosen_images(images);
        textViewImagesCounter.setText(list.size() + " Images chosen");
    }

    View.OnClickListener addProductClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeKeyboard();

            boolean flag;
            String name = editTextProductName.getText().toString();
            double price = Double.parseDouble(editTextProductPrice.getText().toString().equals("") ? "0.0" : editTextProductPrice.getText().toString());
            String description = editTextProductDescription.getText().toString();
            int quantity = Integer.parseInt(editTextProductQuantity.getText().toString().equals("") ? "0" : editTextProductQuantity.getText().toString());
            int category = spinnerProductCategory.getSelectedItemPosition();
//            String category = spinnerProductCategory.getSelectedItem().toString();

            flag = addProductViewModel.checkName(name, editTextProductName);
            flag &= addProductViewModel.checkImages();
            flag &= addProductViewModel.checkPrice(price, editTextProductPrice);
            flag &= addProductViewModel.checkDescription(description, editTextProductDescription);
            flag &= addProductViewModel.checkQuantity(quantity, editTextProductQuantity);
            flag &= addProductViewModel.checkCategory(category);

            if (flag) {
                progressBarAddProduct.setVisibility(View.VISIBLE);
                Product product = new Product();
                product.setName(name);
                product.setPrice(price);
                product.setDescription(description);
                product.setQuantity(quantity);
                product.setCategory(addProductViewModel.getCategories().getValue().get(category - 1).getId());

                addProductViewModel.addProduct(product);

                addProductViewModel.getSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Toast.makeText(getActivity(), aBoolean.toString(), Toast.LENGTH_SHORT).show();
                        addProductViewModel.setSuccess(new MutableLiveData<>());
                        progressBarAddProduct.setVisibility(View.GONE);
                        resetAllViews();
                    }
                });
            }
        }
    };

    public void resetAllViews(){
        editTextProductName.setText("");
        addProductViewModel.setChosen_images(null);
        textViewImagesCounter.setText("");
        editTextProductPrice.setText("");
        editTextProductDescription.setText("");
        editTextProductQuantity.setText("");
        spinnerProductCategory.setSelection(0);
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}