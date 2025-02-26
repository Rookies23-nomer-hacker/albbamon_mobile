package com.example.albbamon.api;

import com.example.albbamon.dto.request.ResumeRequestDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ResumeAPI {
    @POST("/api/resume/write")  // 서버에 이력서 저장 요청
    Call<String> createResume(@Body ResumeRequestDto resumeRequestDto);
}
