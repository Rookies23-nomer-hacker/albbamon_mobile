package com.example.albbamon.api;

import com.example.albbamon.model.RecruitmentDetailResponse;
import com.example.albbamon.model.JobPostingModel;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.network.SuccessResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RecruitmentAPI {
    @GET("/api/recruitment/list")  // ✅ 전체 JSON 객체를 받도록 수정
    Call<RecruitmentResponse> getRecruitmentPosts();

    @Multipart
    @POST("/api/mobile/recruitment")
    Call<SuccessResponse<Void>> createRecruitment(@Part MultipartBody.Part file, @Part("requestDto") JobPostingModel jobPostingModel);

    @GET("/api/recruitment/{id}") // ✅ 단일 공고 조회
    Call<RecruitmentDetailResponse> getRecruitmentDetails(@Path("id") Long id);

    @POST("/api/mobile/recruitment/{recruitmentId}/apply")
    Call<ResponseWrapper2<Void>> applyForJob(
            @Path("recruitmentId") long recruitmentId,
            @Query("userId") long userId
    );

}


