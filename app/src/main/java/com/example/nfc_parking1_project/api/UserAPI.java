package com.example.nfc_parking1_project.api;

import com.example.nfc_parking1_project.model.Auth;
import com.example.nfc_parking1_project.model.User;
import com.example.nfc_parking1_project.retrofitconfig.RetrofitConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {
    RetrofitConfig retrofit = new RetrofitConfig();
    UserAPI userApi = retrofit.RetrofitConfigure().create(UserAPI.class);

    @Headers("Content-Type: application/json")
    @POST("api/auth/register")
    Call<MessageResponse> createStaff(@Header("Authorization") String token, @Body User user);

    @GET("api/user")
    Call<List<User>> getAllUser(@Header("Authorization") String token);

    @PUT("api/user/{id}")
    Call<MessageResponse> updateUserInfo(@Header("Authorization") String token, @Path("id") int id, @Body User user);


    @PUT("api/editUser/change-password")
    Call<MessageResponse> changePassword(@Header("Authorization") String token, @Body Auth request);

    @PUT("api/editUser/reset-staffPassword")
    Call<MessageResponse> resetStaffPassword(@Header("Authorization") String token,@Body Auth request);


}
