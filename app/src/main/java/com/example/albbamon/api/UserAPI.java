package com.example.albbamon.api;

import com.example.albbamon.model.UserModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAPI {
    // 모든 사용자 정보 가져오기
    @GET("/api/user")

    Call<UserModel> getUserInfo();

}