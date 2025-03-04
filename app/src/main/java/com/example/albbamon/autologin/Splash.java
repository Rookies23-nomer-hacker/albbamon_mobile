package com.example.albbamon.autologin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.MainActivity;
import com.example.albbamon.sign.SignInActivity;
import com.example.albbamon.api.UserAPI;
import com.example.albbamon.dto.response.UserResponseDto;
import com.example.albbamon.network.RetrofitClient;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAutoLogin(); //
    }

    // ✅ 자동 로그인 체크
    private void checkAutoLogin() {
        SharedPreferences prefs = getSharedPreferences("ECACHE", MODE_PRIVATE);
        String savedCookie = prefs.getString("email", null);
        Log.d("AUTO_RESPONSE", "여기는 오지?");
        if(savedCookie != null){
            Log.d("AUTO_RESPONSE", "autologin email : " + savedCookie);
        }else{
            Log.d("AUTO_RESPONSE", "저장된 email 없음");
        }

        if (savedCookie == null){
            Log.d("AUTO_RESPONSE", "JSESSION 없어서 로그인으로 이동함");
            navigateToSignIn();
            return;
        }

        UserAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(UserAPI.class);
        Call<ResponseBody> call = apiService.checkCache(savedCookie);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("API_RESPONSE", "HTTP 응답 코드: " + response.code());

                for (String name : response.headers().names()) {
                    Log.d("API_RESPONSE", "헤더: " + name + " = " + response.headers().get(name));
                }

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // 서버에서 응답온 데이터에서 userid 추출
                        String responseBodyString = response.body().string().trim();
                        Log.d("AUTO_RESPONSE", "서버 응답 원본: " + responseBodyString);

                        // JSON을 UserResponseDto로 변환
                        Gson gson = new Gson();
                        UserResponseDto userResponse = gson.fromJson(responseBodyString, UserResponseDto.class);

                        // ✅ userId 가져오기
                        long userId = userResponse.getUserId();
                        Log.d("AUTO_RESPONSE", "✅ 로그인 성공 - userId: " + userId);
                        Log.d("AUTO_RESPONSE", "서버 쿠키: " + response.headers());

                        // ✅ 서버 응답 헤더에서 `Set-Cookie` 가져오기
                        String setCookieHeader = response.headers().get("Set-Cookie");


                        if (setCookieHeader != null) {
                            Log.d("AUTO_RESPONSE", "서버에서 받은 세션 쿠키: " + setCookieHeader);

                            // ✅ SharedPreferences에 세션 쿠키 저장
                            SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("cookie", setCookieHeader); // ✅ 세션 쿠키 저장
                            editor.putLong("userId", userId); // ✅ userId 저장
                            editor.apply();

                            Log.d("AUTO_RESPONSE", "세션 쿠키 저장 완료");
                        } else {
                            Log.e("AUTO_RESPONSE", "서버에서 Set-Cookie 헤더를 반환하지 않음.");
                        }

                        Log.d("AUTO_RESPONSE", "로그인 성공 - userId: " + userId);
                        Toast.makeText(Splash.this, "로그인 성공! ID: " + userId, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Splash.this, MainActivity.class); //MainActivity

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전 화면 제거
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        Log.e("AUTO_RESPONSE", "서버 응답 처리 실패", e);
                    }
                } else {
                    try {
                        // 🚀 서버에서 반환하는 에러 메시지 확인
                        String errorBody = response.errorBody().string();
                        Log.e("AUTO_RESPONSE", "로그인 실패 - 응답 본문: " + errorBody);
                        Toast.makeText(Splash.this, "로그인 실패: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("AUTO_RESPONSE", "에러 본문 읽기 실패", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AUTO_RESPONSE", "네트워크 오류 발생: " + t.getMessage(), t);
                Toast.makeText(Splash.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void navigateToSignIn() {
        Intent intent = new Intent(Splash.this, SignInActivity.class);
        startActivity(intent);
        finish(); // 스플래시 화면 종료
    }

}
