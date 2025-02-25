package com.example.albbamon.repository;

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

    // 생성자에서 Retrofit 인스턴스를 가져옴
    public UserRepository() {
        this.userAPI = RetrofitClient.getRetrofitInstance().create(UserAPI.class);
    }

    // 유저 정보를 가져오는 함수
    public void fetchUserInfo(UserCallback callback) {
        Call<UserModel> call = userAPI.getUserInfo();

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
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
    public void changePassword(Long userId, String oldPw, String newPw, PasswordCallback callback) {
        ChangePwRequestDto request = new ChangePwRequestDto(userId, oldPw, newPw);
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
