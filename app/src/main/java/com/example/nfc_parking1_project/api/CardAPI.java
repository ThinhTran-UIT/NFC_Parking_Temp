package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.Card;
import com.example.nfc_parking1_project.retrofitconfig.RetrofitConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CardAPI {
    RetrofitConfig retrofit = new RetrofitConfig();
    CardAPI cardApi = retrofit.RetrofitConfigure().create(CardAPI.class);

    @Headers("Content-Type: application/json")
    //Get all cards
    @GET("api/cards")
    Call<List<Card>> getCards(@Header("Authorization") String token);

    //Get one card
    @GET("api/cards/{id}")
    Call<Card> getOneCard(@Header("Authorization") String token, @Path("id") String id);

    //Create card
    @POST("api/cards")
    Call<MessageResponse> createCard(@Header("Authorization") String token, @Body Card card);

    //Report card lost
    @POST("api/cards/reportLost/{id}")
    Call<MessageResponse> reportLost(@Header("Authorization") String token, @Path("id") String id);

    //Update available status for lost card
    @PATCH("api/cards/")
    Call<MessageResponse> updateCardLost(@Header("Authorization") String token, @Path("id") int id);

    @PUT("api/cards/recoveryCard/{cardId}")
    Call<MessageResponse> recoveryCard(@Header("Authorization") String token,@Path("cardId") String cardId);


}
