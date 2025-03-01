package com.example.albbamon.api;

import com.example.albbamon.dto.request.UpdateApplyStatusRequestDto;
import com.example.albbamon.model.SuccessResponse;
import com.example.albbamon.dto.response.GetRecruitmentResponseDto;
import com.example.albbamon.dto.response.GetRecruitmentApplyListResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecruitmentAPI {
    @GET("api/mobile/recruitment/list/my")
    Call<SuccessResponse<GetRecruitmentResponseDto>> getMyRecruitments();

    @GET("api/recruitment/{recruitmentId}/apply")
    Call<SuccessResponse<GetRecruitmentApplyListResponseDto>> getRecruitmentApplyList(@Path("recruitmentId") Long recruitmentId);

    @POST("api/recruitment/{recruitmentId}/apply/{applyId}/status")
    Call<String> updateApplyStatus(
            @Path("recruitmentId") Long recruitmentId,
            @Path("applyId") Long applyId,
            @Body UpdateApplyStatusRequestDto requestDto
    );
}