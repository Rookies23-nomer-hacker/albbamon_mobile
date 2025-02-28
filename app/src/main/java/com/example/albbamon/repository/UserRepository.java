package com.example.albbamon.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.albbamon.api.UserAPI;
import com.example.albbamon.dto.request.ChangePwRequestDto;
import com.example.albbamon.dto.response.UserChangePwResponseDto;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.model.UserModel;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.network.SuccessResponse;
import com.google.gson.Gson;

import javax.security.auth.callback.PasswordCallback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserAPI userAPI;

    // 생성자에서 세션 포함된 Retrofit 사용
    public UserRepository(Context context) {
        this.userAPI = RetrofitClient.getRetrofitInstanceWithSession(context).create(UserAPI.class);
    }
    public void fetchUserInfo(UserCallback callback) {
        Log.d("UserRepository", "🚀 [API 요청] fetchUserInfo");

        Call<UserModel> call = userAPI.getUserInfo();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.d("API_RESPONSE", "HTTP 응답 코드: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() != null && response.body().getData().getUserInfo() != null) {
                        callback.onSuccess(response.body().getData().getUserInfo());
                    } else {
                        Log.d("DEBUG", "userInfo가 null입니다.");
                        callback.onFailure("userInfo가 null입니다.");
                    }
                } else {
                    try {
                        Log.e("API_ERROR", "서버 응답 실패 - 코드: " + response.code());
                        Log.e("API_ERROR", "응답 본문: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("API_ERROR", "응답 본문 읽기 실패", e);
                    }
                    callback.onFailure("응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d("DEBUG", "API 호출 실패: " + t.getMessage());
                callback.onFailure("API 호출 실패: " + t.getMessage());
            }
        });
    }

    // 비밀번호 변경 API 호출 메서드 추가
    public void changePassword(String oldPw, String newPw, PasswordCallback callback) {
        // ✅ userId 없이 요청하는 DTO 생성
        ChangePwRequestDto request = new ChangePwRequestDto(oldPw, newPw);

        // ✅ API 요청 (세션 쿠키 필요 없음)
        Call<UserChangePwResponseDto> call = userAPI.changePassword(request);

        call.enqueue(new Callback<UserChangePwResponseDto>() {
            @Override
            public void onResponse(Call<UserChangePwResponseDto> call, Response<UserChangePwResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMessage());
                } else {
                    callback.onFailure("❌ 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserChangePwResponseDto> call, Throwable t) {
                callback.onFailure("🚨 API 호출 실패: " + t.getMessage());
            }
        });
    }

    // ✅ 회원 탈퇴 API 호출 메서드 추가
    public void deleteUser(DeleteUserCallback callback) {
        Log.d("UserRepository", "🚀 [API 요청] 회원 탈퇴");

        Call<SuccessResponse> call = userAPI.deleteUser();

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("회원 탈퇴 성공");
                } else {
                    callback.onFailure("회원 탈퇴 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                callback.onFailure("회원 탈퇴 API 호출 실패: " + t.getMessage());
            }
        });
    }


    // 회원 탈퇴 콜백 인터페이스
    public interface DeleteUserCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    // API 응답 전달 인터페이스
    public interface UserCallback {
        void onSuccess(UserInfo userInfo);
        void onFailure(String errorMessage);
    }

    // 비밀번호 변경 콜백 인터페이스
    public interface PasswordCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}
