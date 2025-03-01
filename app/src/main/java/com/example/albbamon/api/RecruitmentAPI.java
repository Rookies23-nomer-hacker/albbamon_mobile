package com.example.albbamon.api;

import com.example.albbamon.model.RecruitmentApplyRequest;
import com.example.albbamon.model.RecruitmentDetailResponse;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.model.RecruitmentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecruitmentAPI {
    @GET("/api/recruitment/list")  // ✅ 전체 JSON 객체를 받도록 수정
    Call<RecruitmentResponse> getRecruitmentPosts();

    @GET("/api/recruitment/{id}") // ✅ 단일 공고 조회
    Call<RecruitmentDetailResponse> getRecruitmentDetails(@Path("id") Long id);

    @POST("/api/mobile/recruitment/{recruitmentId}/apply")
    Call<ResponseWrapper2<Void>> applyForJob(
            @Path("recruitmentId") long recruitmentId,
            @Query("userId") long userId
    );

}


