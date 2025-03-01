package com.example.albbamon.api;

import com.example.albbamon.model.RecruitmentDetailResponse;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.model.RecruitmentResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecruitmentAPI {
    @GET("/api/recruitment/list")  // ✅ 전체 JSON 객체를 받도록 수정
    Call<RecruitmentResponse> getRecruitmentPosts();

    @GET("/api/recruitment/{id}") // ✅ 단일 공고 조회
    Call<RecruitmentDetailResponse> getRecruitmentDetails(@Path("id") Long id);

}


