package com.example.nfc_parking1_project;

public class User {
    private String username;
    private String phoneNumber;
    private String shopName;

    public User(String username, String phoneNumber, String shopName) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.shopName = shopName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
