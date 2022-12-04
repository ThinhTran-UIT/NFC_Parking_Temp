package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.Card;
import com.example.nfc_parking1_project.retrofitconfig.RetrofitConfig;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CardAPI {
    RetrofitConfig retrofit = new RetrofitConfig();
    CardAPI cardApi = retrofit.RetrofitConfigure().create(CardAPI.class);

    @Headers("Content-Type: application/json")
    //Get all cards
    @GET("api/cards")
    Call<List<Card>> getCards();
    //Get one card
    @GET("api/cards/{id}")
    Call<Card> getOneCard(@Path("id") int id);
    //Create card
    @POST("api/cards")
    Call<MessageResponse> createCard(@Body Card card);
    //Report card lost
    @POST("api/cards/reportLost/{id}")
    Call<MessageResponse> reportLost(@Path("id") int id);

    //Update available status for lost card
    @PATCH("api/cards/")
    Call<MessageResponse> updateCardLost(@Path("id") int id);





}
