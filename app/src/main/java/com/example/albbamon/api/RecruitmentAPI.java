package com.example.albbamon.api;

import com.example.albbamon.model.RecruitmentCountResponse;
import com.example.albbamon.model.RecruitmentDetailResponse;
import com.example.albbamon.model.JobPostingModel;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.network.SuccessResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import com.example.albbamon.dto.request.UpdateApplyStatusRequestDto;
import com.example.albbamon.network.SuccessResponse;
import com.example.albbamon.dto.response.GetRecruitmentResponseDto;
import com.example.albbamon.dto.response.GetRecruitmentApplyListResponseDto;

import okhttp3.ResponseBody;
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
    Call<RecruitmentResponse> getRecruitmentPosts(
        @Query("page") int page,
        @Query("size") int size
    );

    @GET("api/mobile/recruitment/list/my")
    Call<SuccessResponse<GetRecruitmentResponseDto>> getMyRecruitments();

    @GET("/api/recruitment/{id}") // ✅ 단일 공고 조회
    Call<RecruitmentDetailResponse> getRecruitmentDetails(@Path("id") Long id);

    @GET("api/recruitment/{recruitmentId}/apply")
    Call<SuccessResponse<GetRecruitmentApplyListResponseDto>> getRecruitmentApplyList(@Path("recruitmentId") Long recruitmentId);

    @Multipart
    @POST("/api/mobile/recruitment")
    Call<SuccessResponse<Void>> createRecruitment(
            @Part MultipartBody.Part file, // ✅ 이미지 파일
            @Part("requestDto") RequestBody jobPosting // ✅ JSON 데이터를 RequestBody로 변환 후 전송
    );

    @POST("/api/mobile/recruitment/{recruitmentId}/apply")
    Call<ResponseWrapper2<Void>> applyForJob(
            @Path("recruitmentId") long recruitmentId,
            @Query("userId") long userId);

    @POST("recruitment/{recruitmentId}/apply/{applyId}/status")
    Call<ResponseBody> updateApplyStatus(
            @Path("recruitmentId") long recruitmentId,
            @Path("applyId") long applyId,
            @Body UpdateApplyStatusRequestDto requestDto
    );

    @GET("/api/mobile/recruitment/count")
    Call<RecruitmentCountResponse> getMyApplyCount(
            @Query("userId") long userId
    );

    @GET("/api/mobile/recruitment/list")
    Call<RecruitmentResponse> getAllRecruitmentPosts();  // ✅ 새로운 API 추가

}


