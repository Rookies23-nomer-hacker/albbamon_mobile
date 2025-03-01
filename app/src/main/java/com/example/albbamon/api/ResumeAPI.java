package com.example.albbamon.api;

import com.example.albbamon.dto.request.ResumeRequestDto;
import com.example.albbamon.dto.request.ProfileImageRequestDto;
import com.example.albbamon.dto.response.ProfileImageResponseDto;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ResumeAPI {
    @POST("/api/resume/write")  // 서버에 이력서 저장 요청
    Call<String> createResume(@Body ResumeRequestDto resumeRequestDto);

    @POST("/api/mobile/resume/profileImage")
    Call<ProfileImageResponseDto> updateProfileImage(@Body ProfileImageRequestDto profileImageRequestDto);

    // ✅ `resume_id`로 전체 이력서 정보 가져오기
    @GET("/api/mobile/resume/view")
    Call<Map<String, Object>> getResume();

    @POST("/api/mobile/resume")
    Call<Map<String, Object>> getMyResume();

}
