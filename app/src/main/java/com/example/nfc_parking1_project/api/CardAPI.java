package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.helper.NFCParkingConstant;
import com.example.nfc_parking1_project.model.Card;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CardAPI {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    CardAPI cardApi = new Retrofit.Builder().baseUrl(NFCParkingConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(CardAPI.class);
    @Headers("Content-Type: application/json")
    @GET("api/cards")
    Call<List<Card>> getCards();

    @POST("api/cards")
    Call<Response> createCard(@Body Card card);
}
