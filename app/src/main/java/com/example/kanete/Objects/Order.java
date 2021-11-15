package com.example.kanete.Objects;

public class Order {
    private String ID;
    private String store_UID;
    private String customer_UID;
    private String product_ID;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
