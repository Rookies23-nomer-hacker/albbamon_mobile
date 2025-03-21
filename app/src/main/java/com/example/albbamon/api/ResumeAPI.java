package com.example.albbamon.api;

import com.example.albbamon.dto.request.ResumeRequestDto;
import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.model.ResumeModel;
import com.example.albbamon.dto.request.ProfileImageRequestDto;
import com.example.albbamon.dto.response.ProfileImageResponseDto;

import java.util.Map;

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

    @GET("/api/mobile/resume/view")
    Call<ResumeResponseDto> getResume(
            @Query("userId") long userId
    );

    @GET("/api/resume/view")
    Call<Map<String, Object>> getResumeById(@Query("resume_id") long resumeId);

    @GET("/api/resume/{userId}")
    Call<ResponseWrapper2<ResumeModel>> getResumeByUserId(@Path("userId") long userId);

    @POST("/api/resume/write")  // 서버에 이력서 저장 요청
    Call<String> createResume(@Body ResumeRequestDto resumeRequestDto);

    @POST("/api/mobile/resume")
    Call<Map<String, Object>> getMyResume();

    @GET("/api/mobile/resume/delete")
    Call<Void> deleteResume();

    @GET("/api/mobile/resume/check")
    Call<Boolean> checkResumeExists(@Query("userId") long userId);

    @GET("/api/mobile/resume/view")
    Call<Map<String, Object>> getResume();

    @POST("/api/mobile/resume/profileImage")
    Call<ProfileImageResponseDto> updateProfileImage(@Body ProfileImageRequestDto profileImageRequestDto);

    @GET("/api/resume/download")
    Call<ResponseBody> downloadResumeFile(@Query("fileName") String fileName);
}
