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

import javax.security.auth.callback.PasswordCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserAPI userAPI;

    // 생성자에서 세션 포함된 Retrofit 사용
    public UserRepository(Context context) {
        this.userAPI = RetrofitClient.getRetrofitInstanceWithSession(context).create(UserAPI.class);
    }

    // 유저 정보 가져오는 함수 (세션 포함)
    public void fetchUserInfo(Context context, UserCallback callback) {
        // ✅ SharedPreferences에서 세션 쿠키 가져오기
        SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
        String sessionCookie = prefs.getString("cookie", "");

        if (sessionCookie.isEmpty()) {
            callback.onFailure("[DEBUG] 세션 쿠키가 없습니다. 로그인이 필요합니다.");
            return;
        }

        // ✅ API 요청
        Call<UserModel> call = userAPI.getUserInfo(sessionCookie);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.d("API_RESPONSE", "HTTP 응답 코드: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() != null && response.body().getData().getUserInfo() != null) {
                        callback.onSuccess(response.body().getData().getUserInfo());
                    } else {
                        callback.onFailure("[DEBUG] userInfo가 null입니다.");
                    }
                } else {
                    callback.onFailure("[DEBUG][ERROR] 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                callback.onFailure("[DEBUG][ERROR] API 호출 실패: " + t.getMessage());
            }
        });
    }

    // 비밀번호 변경 API 호출 메서드 추가
    public void changePassword(Context context, Long userId, String oldPw, String newPw, PasswordCallback callback) {
        // ✅ SharedPreferences에서 저장된 세션 쿠키 가져오기
        SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
        String sessionCookie = prefs.getString("cookie", "");

        if (sessionCookie.isEmpty()) {
            callback.onFailure("❌ 세션 쿠키가 없습니다. 로그인이 필요합니다.");
            return;
        }

        ChangePwRequestDto request = new ChangePwRequestDto(userId, oldPw, newPw);

        // ✅ 세션 쿠키 포함하여 API 요청
        Call<UserChangePwResponseDto> call = userAPI.changePassword(sessionCookie, request);

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
