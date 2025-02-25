package com.example.albbamon.api;

import com.example.albbamon.model.UserModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

import retrofit2.http.Path;

import com.example.albbamon.dto.request.ChangePwRequestDto;
import com.example.albbamon.dto.response.UserChangePwResponseDto;


public interface UserAPI {
    @GET("/api/user")
    Call<UserModel> getUserInfo();

    @POST("/api/user/change-pw")
    Call<UserChangePwResponseDto> changePassword(@Body ChangePwRequestDto requestDto);
}
