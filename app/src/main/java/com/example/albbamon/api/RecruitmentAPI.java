package com.example.albbamon.api;

import com.example.albbamon.model.SuccessResponse;
import com.example.albbamon.dto.response.GetRecruitmentResponseDto;
import com.example.albbamon.dto.response.GetRecruitmentApplyListResponseDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecruitmentAPI {
    @GET("api/mobile/recruitment/list/my")
    Call<SuccessResponse<GetRecruitmentResponseDto>> getMyRecruitments();

    @GET("api/mobile/recruitment/{recruitmentId}/apply")
    Call<SuccessResponse<GetRecruitmentApplyListResponseDto>> getRecruitmentApplyList(@Path("recruitmentId") Long recruitmentId);
}