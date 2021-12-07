package com.example.kanete.Store.ui.addproducts;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.kanete.Models.Category;
import com.example.kanete.Models.Product;
import com.example.kanete.Models.User;
import com.example.kanete.R;
import com.example.kanete.helper.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddProductViewModel extends ViewModel {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private Activity this_activity;
    private User user;
    private MutableLiveData<Boolean> success;
    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<Uri>> chosen_images;

    public AddProductViewModel(Activity activity) {
        this_activity = activity;
        user = new User();
        success = new MutableLiveData<>();
        categories = initCategories();
        chosen_images = new MutableLiveData<>();
    }

    public MutableLiveData<List<Category>> getCategories() {
        return categories;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    public void setSuccess(MutableLiveData<Boolean> success) {
        this.success = success;
    }

    public List<String> getCategories_name() {
        List<String> list = new ArrayList<>();
        list.add(this_activity.getString(R.string.AddProductFragment_category_ex));
        for (Category category :
                categories.getValue()) {
            list.add(category.getName());
        }
        return list;
    }

    public List<Uri> getChosen_images() {
        return chosen_images.getValue();
    }

    public void setChosen_images(List<Uri> images) {
        chosen_images.postValue(images);
    }

    public MutableLiveData<List<Category>> initCategories(){
        Category category = new Category();
        return category.getAllCategories();
    }

    public boolean allowPermission(){ // TODO maybe change this
        if (ActivityCompat.checkSelfPermission(this_activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this_activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this_activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void addProduct(Product product){
        product.setStore_UID(user.getUID());
        product.setDate_added(Utils.getCurrentDate());
        product.addProduct(product, getChosen_images()).observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                success.postValue(aBoolean);
            }
        });
    }

    // Value Checks
    public boolean checkCategory(int category){
        if (category == 0){
            Toast.makeText(this_activity, "Please chose category!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkQuantity(int quantity, EditText editText){
        if (quantity <= 0){
            editText.setError("Quantity must be more than one!");
            return false;
        }
        return true;
    }

    public boolean checkImages() {
        if (getChosen_images() == null){
            Toast.makeText(this_activity, "Please add at least one image!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkName(String name, EditText editText) {
        if (name.isEmpty()){
            editText.setError("Please fill!");
            return false;
        }
        return true;
    }

    public boolean checkPrice(double price, EditText editText) {
        if (price == 0){
            editText.setError("Please fill!");
            return false;
        }
        return true;
    }

    public boolean checkDescription(String description, EditText editText) {
        if (description.isEmpty()){
            editText.setError("Please fill!");
            return false;
        }
        return true;
    }
}