package com.example.albbamon.api;

import com.example.albbamon.model.ResumeUserModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserAPI {
    // 모든 사용자 정보 가져오기
    @GET("/api/user")

    Call<ResumeUserModel> getUserInfo();

}