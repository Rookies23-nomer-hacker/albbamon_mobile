package com.example.albbamon.api;

import com.example.albbamon.dto.request.ChangePwRequestDto;
import com.example.albbamon.dto.response.UserChangePwResponseDto;
import com.example.albbamon.model.UserModel;
import com.example.albbamon.network.SuccessResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.albbamon.model.LoginUserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {

    @GET("/api/user")
    Call<UserModel> getUserInfo();

    @POST("/api/user/change-pw")
    Call<UserChangePwResponseDto> changePassword(@Body ChangePwRequestDto requestDto);

    @POST("/api/user/sign-in")
    Call<ResponseBody> signIn(@Body LoginUserModel login);

    @GET("/api/user/withdraw")
    Call<SuccessResponse> deleteUser(@Query("userId") long userId);
}
