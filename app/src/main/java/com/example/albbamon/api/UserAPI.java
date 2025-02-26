package com.example.albbamon.api;

import com.example.albbamon.dto.request.ChangePwRequestDto;
import com.example.albbamon.dto.response.UserChangePwResponseDto;
import com.example.albbamon.model.UserModel;
import com.example.albbamon.model.LoginUserModel;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.DELETE;


public interface UserAPI {

    @GET("/api/mobile/user")
    Call<UserModel> getUserInfo();

    @POST("/api/user/change-pw")
    Call<UserChangePwResponseDto> changePassword(@Body ChangePwRequestDto requestDto);

    @POST("/api/user/sign-in")
    Call<ResponseBody> signIn(@Body LoginUserModel login);

    @DELETE("/api/user/{userId}")
    Call<ResponseBody> deleteUser(@Header("Cookie") String sessionCookie, @Path("userId") long userId);

//    @GET("/api/user/withdraw")
//    Call<SuccessResponse> deleteUser(@Query("userId") long userId);
}