package com.example.kanete.Models;

import com.google.firebase.firestore.Exclude;

public class Order {
    private String ID;
    private String store_UID;
    private String customer_UID;
    private String product_ID;
    private String address_ID;
    private String Payment_ID;
    private int quantity;
    private String status;
    private boolean complete;

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

    public String getCustomer_UID() {
        return customer_UID;
    }

    public void setCustomer_UID(String customer_UID) {
        this.customer_UID = customer_UID;
    }

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public String getAddress_ID() {
        return address_ID;
    }

    public void setAddress_ID(String address_ID) {
        this.address_ID = address_ID;
    }

    public String getPayment_ID() {
        return Payment_ID;
    }

    public void setPayment_ID(String payment_ID) {
        Payment_ID = payment_ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
