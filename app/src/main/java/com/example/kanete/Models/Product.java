package com.example.kanete.Models;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Product implements Serializable {
    private String ID;
    private String store_UID;
    private String name;
    private List<String> images;
    private String description;
    private String date_added;
    private double price;
    private int quantity;
    private String category;

    @Exclude
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStore_UID() {
        return store_UID;
    }

    public void setStore_UID(String store_UID) {
        this.store_UID = store_UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Exclude
    public LiveData<Boolean> addProduct(Product product, List<Uri> images){
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
        CollectionReference db = FirebaseFirestore.getInstance().collection("Products");
        String ID = db.document().getId();
        uploadImages(ID, images).observeForever(new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> img_urls) {
                if (img_urls.size() == images.size()){
                    product.setImages(img_urls);
                    db.document(ID)
                            .set(product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    booleanMutableLiveData.postValue(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    booleanMutableLiveData.postValue(false);
                                }
                            });
                }
            }
        });

        return booleanMutableLiveData;
    }

    @Exclude
    public MutableLiveData<List<String>> uploadImages(String product_id, List<Uri> images){
        MutableLiveData<List<String>> img_urls = new MutableLiveData<>(new ArrayList<>());
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        for (Uri image :
                images) {
            StorageReference currentRef = storageRef.child("Product_Images/" + product_id + "/" + UUID.randomUUID().toString());
            currentRef.putFile(image)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            currentRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    List<String> cur = img_urls.getValue();
                                    cur.add(url);
                                    img_urls.postValue(cur);
                                }
                            });
                        }
                    }
                });
        }
        return img_urls;
    }

    @Exclude
    public MutableLiveData<List<Product>> getAllProducts() {
        MutableLiveData<List<Product>> products = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Products")
                .orderBy("date_added", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot :
                                queryDocumentSnapshots.getDocuments()) {
                            Product product = documentSnapshot.toObject(Product.class);
                            product.setID(documentSnapshot.getId());
                            productList.add(product);
                        }
                        products.postValue(productList);
                    }
                });
        return products;
    }

    @Exclude
    public MutableLiveData<List<Product>> getMyProducts() {
        MutableLiveData<List<Product>> products = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Products")
                .orderBy("date_added", Query.Direction.DESCENDING)
                .whereEqualTo("store_UID", new User().getUID())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<Product> productList = new ArrayList<>();
                        if (value != null) { // TODO fix
                            for (DocumentSnapshot documentSnapshot :
                                    value.getDocuments()) {
                                Product product = documentSnapshot.toObject(Product.class);
                                product.setID(documentSnapshot.getId());
                                productList.add(product);
                            }
                            products.postValue(productList);
                        }
                    }
                });
        return products;
    }

    @Exclude
    public MutableLiveData<List<String>> getProductImages(){
        MutableLiveData<List<String>> images = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Products")
                .document(this.ID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Product product = documentSnapshot.toObject(Product.class);
                        images.postValue(product.getImages());
                    }
                });
        return images;
    }
}
