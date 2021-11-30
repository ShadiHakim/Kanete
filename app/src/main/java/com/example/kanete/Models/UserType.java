package com.example.kanete.Models;

public class UserType {
    public static enum types {Customer, Store}

    private types type;

    public UserType() {
    }

    public UserType(types type) {
        this.type = type;
    }

    public types getType() {
        return type;
    }

    public void setType(types type) {
        this.type = type;
    }
}
