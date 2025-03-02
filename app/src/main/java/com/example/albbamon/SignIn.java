package com.example.albbamon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.albbamon.api.UserAPI;
import com.example.albbamon.dto.response.UserResponseDto;
import com.example.albbamon.model.LoginUserModel;
import com.example.albbamon.network.RetrofitClient;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Base64;

public class SignIn extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setEnabled(true);

        // 🔹 뒤로가기 버튼 클릭 이벤트 추가
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // 회원가입 버튼 클릭 시 account 화면으로 이동
        TextView textView = findViewById(R.id.signUp);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), account.class);
            startActivity(intent);
        });

        // 로그인 버튼 클릭 이벤트
        loginBtn.setOnClickListener(v -> loginUser());

        // ✅ 자동 로그인 검증 로직
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        String sessionCookie = prefs.getString("cookie", null);
        long userId = prefs.getLong("userId", -1);

        SharedPreferences eCache = getSharedPreferences("ECACHE", MODE_PRIVATE);
        String encodedEmail = eCache.getString("email", null);

        if (sessionCookie != null && userId != -1 && encodedEmail != null) {
            Log.d("AUTO_LOGIN", "✅ 자동 로그인 수행");
            Intent intent = new Intent(SignIn.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 입력값 로그 출력
//        Log.d("LOGIN_INPUT", "이메일: " + email + ", 비밀번호: " + password);

        UserAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(UserAPI.class);
        LoginUserModel login = new LoginUserModel(email, password);

        // 서버에서 단순 문자열을 반환하므로 ResponseBody 사용
        Call<ResponseBody> call = apiService.signIn(login);

        // API 요청 로그
        Log.d("API_REQUEST", "로그인 API 요청 전송: " + new Gson().toJson(login));

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
                        Log.d("API_RESPONSE", "서버 응답 원본: " + responseBodyString);

                        // JSON을 UserResponseDto로 변환
                        Gson gson = new Gson();
                        UserResponseDto userResponse = gson.fromJson(responseBodyString, UserResponseDto.class);

                        // ✅ userId 가져오기
                        long userId = userResponse.getUserId();
                        String email = userResponse.getEmail();
                        Log.d("API_RESPONSE", "✅ 로그인 성공 - userId: " + userId);
                        Log.d("API_RESPONSE", "서버 쿠키: " + response.headers());

                        // ✅ 서버 응답 헤더에서 `Set-Cookie` 가져오기
                        String setCookieHeader = response.headers().get("Set-Cookie");

                        CheckBox autoLoginCheck = findViewById(R.id.autoLogin);
                        Boolean autoCheck = autoLoginCheck.isChecked();

                        if (setCookieHeader != null) {
                            Log.d("SESSION", "서버에서 받은 세션 쿠키: " + setCookieHeader);

                            // ✅ SharedPreferences에 세션 쿠키 저장
                            SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("cookie", setCookieHeader); // ✅ 세션 쿠키 저장
                            editor.putLong("userId", userId); // ✅ userId 저장
                            editor.apply();

                            if (autoCheck){
                                SharedPreferences eCache = getSharedPreferences("ECACHE", MODE_PRIVATE);
                                SharedPreferences.Editor cacheEditor = eCache.edit();
                                String encodedEmail = Base64.encodeToString(email.getBytes(), Base64.NO_WRAP);

                                Log.d("API_RESPONSE", encodedEmail);

                                cacheEditor.putString("email", encodedEmail); //email 캐시 저장
                                cacheEditor.apply();
                                Log.d("auto", "이메일 저장 완료");

                                String savedEmail = eCache.getString("email", "default_value");
                                Log.d("auto", "저장된 이메일 확인: " + savedEmail);
                            }

                            Log.d("SESSION", "세션 쿠키 저장 완료");
                        } else {
                            Log.e("SESSION", "서버에서 Set-Cookie 헤더를 반환하지 않음.");
                        }

                        Log.d("API_RESPONSE", "로그인 성공 - userId: " + userId);
                        Toast.makeText(SignIn.this, "로그인 성공! ID: " + userId, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignIn.this, MainActivity.class); //MainActivity
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전 화면 제거
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        Log.e("API_ERROR", "서버 응답 처리 실패", e);
                    }
                } else {
                    try {
                        // 🚀 서버에서 반환하는 에러 메시지 확인
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "로그인 실패 - 응답 본문: " + errorBody);
                        Toast.makeText(SignIn.this, "로그인 실패: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API_ERROR", "에러 본문 읽기 실패", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR", "네트워크 오류 발생: " + t.getMessage(), t);
                Toast.makeText(SignIn.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}