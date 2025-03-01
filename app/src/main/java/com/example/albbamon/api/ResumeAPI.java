package com.example.albbamon.api;

import com.example.albbamon.dto.request.ResumeRequestDto;
import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.model.ResumeModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ResumeAPI {
    @POST("/api/mobile/resume/write")
    Call<ResponseBody> saveResume(
            @Query("userId") long userId,
            @Body ResumeRequestDto resumeRequest
    );

    @GET("/api/resume/{userId}")
    Call<ResponseWrapper2<ResumeModel>> getResumeByUserId(@Path("userId") long userId);

}
