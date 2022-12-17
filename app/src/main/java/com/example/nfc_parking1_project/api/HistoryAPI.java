package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.History;
import com.example.nfc_parking1_project.retrofitconfig.RetrofitConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HistoryAPI {
    RetrofitConfig retrofit = new RetrofitConfig();
    HistoryAPI historyApi = retrofit.RetrofitConfigure().create(HistoryAPI.class);

    @Headers("Content-Type: application/json")
    @GET("/api/history/")
    Call<List<History>> getHistories(@Header("Authorization") String token);

    @POST("api/history")
    Call<MessageResponse> createHistory(@Header("Authorization") String token, @Body History history);


    @POST("api/history/query/currentHistory")
    Call<List<History>> getCurrentHistory(@Header("Authorization") String token);


    @POST("api/history/query/lostCardHistory")
    Call<List<History>> getLostCardHistory(@Header("Authorization") String token);

    @GET("api/history/{id}")
    Call<History> getHistory(@Header("Authorization") String token, @Path("id") String id);


    @POST("api/history/vehicleOut/{id}")
    Call<MessageResponse> vehicleOut(@Header("Authorization") String token, @Path("id") int id);
}
