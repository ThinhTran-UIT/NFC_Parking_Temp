package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.Card;
import com.example.nfc_parking1_project.retrofitconfig.RetrofitConfig;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CardAPI {
    RetrofitConfig retrofit = new RetrofitConfig();
    CardAPI cardApi = retrofit.RetrofitConfigure().create(CardAPI.class);
    @Headers("Content-Type: application/json")
    @GET("api/cards")
    Call<List<Card>> getCards();

    @POST("api/cards")
    Call<MessageResponse> createCard(@Body Card card);
}
