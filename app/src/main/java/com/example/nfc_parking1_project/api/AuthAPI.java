package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.Auth;
import com.example.nfc_parking1_project.model.User;
import com.example.nfc_parking1_project.retrofitconfig.RetrofitConfig;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthAPI {
    RetrofitConfig retrofit = new RetrofitConfig();
    AuthAPI authApi = retrofit.RetrofitConfigure().create(AuthAPI.class);

    @Headers("Content-Type: application/json")
    //Get all cards
    @POST("api/auth/login")
    Call<AuthRespone> login(@Body Auth auth);



}
