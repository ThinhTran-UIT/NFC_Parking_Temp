package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.Card;
import com.example.nfc_parking1_project.model.History;
import com.example.nfc_parking1_project.retrofitconfig.RetrofitConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HistoryAPI {
    RetrofitConfig retrofit = new RetrofitConfig();
    HistoryAPI historyApi = retrofit.RetrofitConfigure().create(HistoryAPI.class);

    @Headers("Content-Type: application/json")
    @GET("api/history")
    Call<List<History>> getHistories();





}
