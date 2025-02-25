package com.example.albbamon.network;

import com.example.albbamon.model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApiService {
    @GET("/api/user/withdraw")
    Call<SuccessResponse> deleteUser(@Query("userId") long userId);

   @GET("/api/user/find-id")
   Call<List<UserModel>> findUserId(
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("ceoNum") String ceoNum);
}



