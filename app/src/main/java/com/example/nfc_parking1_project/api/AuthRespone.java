package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.User;
import com.google.gson.annotations.SerializedName;

public class AuthRespone {
    private boolean success;
    private String message;
    private User user;
    @SerializedName("access_token")
    private String token;

    public AuthRespone(boolean success, String message, User user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
