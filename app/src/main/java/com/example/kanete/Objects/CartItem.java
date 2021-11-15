package com.example.kanete.Objects;

public class CartItem {
    private String ID;
    private String customer_UID;
    private String product_ID;
    private int quantity;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
