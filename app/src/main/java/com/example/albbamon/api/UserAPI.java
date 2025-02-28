package com.example.albbamon.api;

import com.example.albbamon.dto.request.ChangePwRequestDto;
import com.example.albbamon.dto.response.UserChangePwResponseDto;
import com.example.albbamon.dto.response.UserResponseDto;
import com.example.albbamon.model.UserFindIdModel;
import com.example.albbamon.model.UserModel;
import com.example.albbamon.model.LoginUserModel;
import com.example.albbamon.network.SuccessResponse;

import java.util.List;
import okhttp3.MultipartBody;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.DELETE;


public interface UserAPI {

    @GET("/api/mobile/user")
    Call<UserModel> getUserInfo();

    @POST("/api/mobile/user/change-pw")
    Call<UserChangePwResponseDto> changePassword(@Body ChangePwRequestDto requestDto);

    @POST("/api/user/sign-in")
    Call<ResponseBody> signIn(@Body LoginUserModel login);

    @GET("/api/mobile/user/withdraw")
    Call<SuccessResponse> deleteUser();

    @GET("/api/mobile/user/autologin")
    Call<ResponseBody> checkCache(@Query("email") String email);


    // 아이디 찾기 API 예시 (GET 요청)
    @GET("/api/user/find-id")
    Call<List<UserFindIdModel>> findUserId(
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("ceoNum") String ceoNum);
}