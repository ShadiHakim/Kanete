package com.example.kanete.Models;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String id;
    private String name;
    private String img;

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MutableLiveData<List<Category>> getAllCategories(){
        MutableLiveData<List<Category>> categories = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Category> categoryList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot :
                                queryDocumentSnapshots.getDocuments()) {
                            Category category = documentSnapshot.toObject(Category.class);
                            category.setId(documentSnapshot.getId());
                            categoryList.add(category);
                        }
                        categories.postValue(categoryList);
                    }
                });
        return categories;
    }
}
