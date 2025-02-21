package com.example.albbamon.api;

import com.example.albbamon.model.UserModel;
import com.example.albbamon.network.SuccessResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPI {
    // 모든 사용자 정보 가져오기
    @GET("/api/user")

    Call<UserModel> getUserInfo();

    @GET("/api/user/withdraw")
    Call<SuccessResponse> deleteUser(@Query("userId") long userId);
}
