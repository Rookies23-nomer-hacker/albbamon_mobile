package com.example.albbamon.api;

import com.example.albbamon.model.RecruitmentResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecruitmentAPI {
    @GET("/api/recruitment/list")  // ✅ 전체 JSON 객체를 받도록 수정
    Call<RecruitmentResponse> getRecruitmentPosts();

}


