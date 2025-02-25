package com.example.albbamon.api;
import com.example.albbamon.model.SignUpModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUpAPI {


    @POST("/api/user")
    Call<SignUpModel> createUser(@Body SignUpModel signUpModel);
}

