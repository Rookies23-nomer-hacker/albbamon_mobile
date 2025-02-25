package com.example.albbamon.api;

import com.example.albbamon.dto.request.ChangePwRequestDto;
import com.example.albbamon.dto.response.UserChangePwResponseDto;
import com.example.albbamon.model.UserModel;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import com.example.albbamon.model.LoginUserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {

    @GET("/api/user")
    Call<UserModel> getUserInfo(@Header("Cookie") String cookie);

    @POST("/api/user/change-pw")
    Call<UserChangePwResponseDto> changePassword(
            @Header("Cookie") String cookie, @Body ChangePwRequestDto requestDto
    );

    @POST("/api/user/sign-in")
    Call<ResponseBody> signIn(@Body LoginUserModel login);

}
