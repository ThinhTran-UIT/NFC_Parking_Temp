package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.Customer;
import com.example.nfc_parking1_project.retrofitconfig.RetrofitConfig;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface CustomerAPI {
    RetrofitConfig retrofit = new RetrofitConfig();
    CustomerAPI customerApi = retrofit.RetrofitConfigure().create(CustomerAPI.class);

    @Headers("Content-Type: application/json")
    @GET("api/customer/{cardId}")
    Call<Customer> getCustomer(@Header("Authorization") String token, @Path("cardId") String cardId);

}
