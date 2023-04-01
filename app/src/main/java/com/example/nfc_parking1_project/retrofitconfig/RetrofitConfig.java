package com.example.nfc_parking1_project.retrofitconfig;

import com.example.nfc_parking1_project.helper.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    public RetrofitConfig() {
    }

    ;

    public Retrofit RetrofitConfigure() {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        return new Retrofit.Builder().baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

