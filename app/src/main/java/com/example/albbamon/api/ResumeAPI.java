package com.example.albbamon.api;

import com.example.albbamon.dto.request.ResumeRequestDto;
import com.example.albbamon.dto.response.ResumeResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ResumeAPI {
    @POST("/api/mobile/resume/write")
    Call<ResumeResponseDto> saveResume(
            @Query("userId") long userId,
            @Body ResumeRequestDto resumeRequest
    );
}
