package com.example.albbamon.api;

import com.example.albbamon.model.JobPostingModel;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.network.SuccessResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RecruitmentAPI {
    @GET("/api/recruitment/list")  // ✅ 전체 JSON 객체를 받도록 수정
    Call<RecruitmentResponse> getRecruitmentPosts();

    @Multipart
    @POST("/api/mobile/recruitment")
    Call<SuccessResponse<Void>> createRecruitment(@Part MultipartBody.Part file, @Part("requestDto") JobPostingModel jobPostingModel);

}


