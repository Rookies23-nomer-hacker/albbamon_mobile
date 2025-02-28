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

import javax.security.auth.callback.PasswordCallback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserAPI userAPI;
    private final SharedPreferences prefs;

    // 생성자에서 세션 포함된 Retrofit 사용
    public UserRepository(Context context) {
        this.userAPI = RetrofitClient.getRetrofitInstanceWithSession(context).create(UserAPI.class);
        this.prefs = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE); // ✅ SharedPreferences 초기화
    }

    // ✅ SharedPreferences에서 userId 가져오기 (동기적으로 즉시 반환)
    public long getUserId() {
        return prefs.getLong("userId", 0L); // 저장된 userId 반환 (없으면 기본값 0)
    }

    // ✅ SharedPreferences에 userId 저장하는 메서드 추가 (fetchUserInfo() 실행 후 저장 필요)
    private void saveUserId(long userId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("userId", userId);
        editor.apply();
    }

    // ✅ fetchUserInfo() 실행 후 userId 저장
    public void fetchUserInfo(UserCallback callback) {
        Log.d("DEBUG", "🚀 fetchUserInfo() 호출됨");

        Call<UserModel> call = userAPI.getUserInfo();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.d("DEBUG", "📌 API 응답 코드: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData() != null && response.body().getData().getUserInfo() != null) {
                        UserInfo userInfo = response.body().getData().getUserInfo();
                        Log.d("DEBUG", "✅ fetchUserInfo() 성공, userId: " + userInfo.getId());

                        if (userInfo.getId() != 0) {
                            saveUserId(userInfo.getId());
                            callback.onSuccess(userInfo);
                        } else {
                            Log.e("ERROR", "❌ userId가 0입니다.");
                            callback.onFailure("userId가 0입니다.");
                        }
                    } else {
                        Log.e("ERROR", "❌ userInfo가 null입니다.");
                        callback.onFailure("userInfo가 null입니다.");
                    }
                } else {
                    Log.e("ERROR", "❌ 응답 실패: " + response.code());
                    callback.onFailure("응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("ERROR", "🚨 API 호출 실패: " + t.getMessage());
                callback.onFailure("API 호출 실패: " + t.getMessage());
            }
        });
    }


    // ✅ 비밀번호 변경 API 호출 메서드 (변경 없음)
    public void changePassword(Context context, Long userId, String oldPw, String newPw, PasswordCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
        String sessionCookie = prefs.getString("cookie", "");

        if (sessionCookie.isEmpty()) {
            callback.onFailure("❌ 세션 쿠키가 없습니다. 로그인이 필요합니다.");
            return;
        }

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

    // ✅ 회원 탈퇴 API 호출 메서드 (변경 없음)
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

    // 기존 인터페이스 변경 없음
    public interface DeleteUserCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public interface UserCallback {
        void onSuccess(UserInfo userInfo);
        void onFailure(String errorMessage);
    }

    public interface PasswordCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}
