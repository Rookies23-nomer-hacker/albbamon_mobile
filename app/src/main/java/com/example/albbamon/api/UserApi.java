package com.example.albbamon.api;


import com.example.albbamon.model.SignUpModel;
import com.example.albbamon.model.UserModel;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

//    // 모든 사용자 정보 가져오기
//    @GET("/api/user")
//
//    Call<UserModel> getUserInfo();
//
//    @GET("/api/user/withdraw")
//    Call<SuccessResponse> deleteUser(@Query("userId") long userId);

//    @POST("/sign-in")
@POST("/api/user/sign-in")
Call<ResponseBody> signIn(@Body UserModel.Login login);


}
