package com.example.albbamon.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApiService {
    @GET("/api/user/withdraw")
    Call<SuccessResponse> deleteUser(@Query("userId") long userId);
}

