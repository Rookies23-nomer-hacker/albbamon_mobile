package com.example.albbamon.sign;

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

import com.example.albbamon.FindIdPersonalActivity;
import com.example.albbamon.FindPwPersonalActivity;
import com.example.albbamon.MainActivity;
import com.example.albbamon.R;
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

public class SignInActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private Integer pwChkNum;

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
            Intent intent = new Intent(getApplicationContext(), SignUpIntroActivity.class);
            startActivity(intent);
        });

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setEnabled(true);

        // 🔹 "아이디 찾기" 버튼 클릭 시 FindIdPersonalActivity 이동
        TextView findIdTextView = findViewById(R.id.findId);  // XML에서 ID 찾아오기
        findIdTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, FindIdPersonalActivity.class);
            startActivity(intent);
        });

        // 🔹 "비밀번호 찾기" 버튼 클릭 시 FindPwPersonalActivity 이동
        TextView findPwTextView = findViewById(R.id.findPw);  // XML에서 ID 찾아오기
        findPwTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, FindPwPersonalActivity.class);
            startActivity(intent);
        });
        // 로그인 버튼 클릭 이벤트
        loginBtn.setOnClickListener(v -> loginUser());

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

        UserAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(UserAPI.class);
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
                    Log.d("API_RESPONSE", "헤더123: " + name + " = " + response.headers().get(name));
                }

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // 서버에서 응답온 데이터에서 userid 추출
                        String responseBodyString = response.body().string().trim();
                        Log.d("API_RESPONSE", "서버 응답 원본: " + responseBodyString);

                        // JSON을 UserResponseDto로 변환
                        Gson gson = new Gson();
                        UserResponseDto userResponse = gson.fromJson(responseBodyString, UserResponseDto.class);

                        // 🚨 비밀번호 검증 실패 여부 확인 (pwChkNum 값이 1 이상이면 실패한 적 있음)
                        if (userResponse.getPwChkNum() != null && userResponse.getPwChkNum() > 0) {
                            Log.e("LOGIN_ERROR", "비밀번호가 틀림 (pwChkNum: " + userResponse.getPwChkNum() + ")");
                            Toast.makeText(SignInActivity.this, "로그인 실패: 비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                            return; // 🚨 로그인 중단
                        }

                        // 🚨 계정이 잠긴 경우 (pwCheck == true)
                        if (userResponse.getPwCheck() != null && userResponse.getPwCheck()) {
                            Log.e("LOGIN_ERROR", "계정이 잠김 (pwCheck: true)");
                            Toast.makeText(SignInActivity.this, "로그인 실패: 계정이 잠겼습니다. 관리자에게 문의하세요.", Toast.LENGTH_LONG).show();
                            return; // 🚨 로그인 중단
                        }

                        String getPwCheck = String.valueOf(userResponse.getPwChkNum());
                        Log.d("userResponse", String.valueOf(userResponse.getPwChkNum()));

                        if(userResponse.getPwChkNum()>0){
                            pwChkNum += 1;
                            Toast.makeText(SignInActivity.this, "로그인 실패(틀린 횟수 :" + pwChkNum + " / 5", Toast.LENGTH_SHORT).show();                        }

                        // ✅ userId 가져오기
                        long userId = userResponse.getUserId();
                        String email = userResponse.getEmail();

                        Log.d("API_RESPONSE", "✅ 로그인 성공 - userId: " + userId);
                        Log.d("API_RESPONSE", "서버 쿠키: " + response.headers());

                        // ✅ 서버 응답 헤더에서 `Set-Cookie` 가져오기
                        String setCookieHeader = response.headers().get("Set-Cookie");

//                        CheckBox autoLoginCheck = findViewById(R.id.autoLogin);
//                        Boolean autoCheck = autoLoginCheck.isChecked();

                        if (setCookieHeader != null) {
                            Log.d("SESSION", "서버에서 받은 세션 쿠키: " + setCookieHeader);

                            // ✅ SharedPreferences에 세션 쿠키 저장
                            SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("cookie", setCookieHeader); // ✅ 세션 쿠키 저장
                            editor.putLong("userId", userId); // ✅ userId 저장
                            
                            // jsession, awsabl, AWSALBCORS 추가
                            if (setCookieHeader.contains("JSESSIONID=")) {
                                String jsessionId = setCookieHeader.split("JSESSIONID=")[1].split(";")[0].trim();
                                editor.putString("jsessionid", jsessionId);
                                Log.d("SESSION", "✅ 정리된 JSESSIONID 저장: " + jsessionId);
                            }
                            if (setCookieHeader.contains("AWSALB=")) {
                                String awsAlb = setCookieHeader.split("AWSALB=")[1].split(";")[0];
                                editor.putString("AWSALB", awsAlb);
                                Log.d("SESSION", "✅ AWSALB 저장: " + awsAlb);
                            }
                            if (setCookieHeader.contains("AWSALBCORS=")) {
                                String awsAlbCors = setCookieHeader.split("AWSALBCORS=")[1].split(";")[0];
                                editor.putString("AWSALBCORS", awsAlbCors);
                                Log.d("SESSION", "✅ AWSALBCORS 저장: " + awsAlbCors);
                            }
                            
                            editor.apply();


                            SharedPreferences eCache = getSharedPreferences("ECACHE", MODE_PRIVATE);
                            SharedPreferences.Editor cacheEditor = eCache.edit();
                            String encodedEmail = Base64.encodeToString(email.getBytes(), Base64.NO_WRAP);

                            Log.d("API_RESPONSE", encodedEmail);

                            cacheEditor.putString("email", encodedEmail); //email 캐시 저장
                            cacheEditor.apply();
                            Log.d("auto", "이메일 저장 완료");

                            String savedEmail = eCache.getString("email", "default_value");
                            Log.d("auto", "저장된 이메일 확인: " + savedEmail);


                            Log.d("SESSION", "세션 쿠키 저장 완료");
                        } else {
                            Log.e("SESSION", "서버에서 Set-Cookie 헤더를 반환하지 않음.");
                        }

                        Log.d("API_RESPONSE", "로그인 성공 - userId: " + userId);
                        Toast.makeText(SignInActivity.this, "로그인 성공! ID: " + userId, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignInActivity.this, MainActivity.class); //MainActivity
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
                        Toast.makeText(SignInActivity.this, "로그인 실패: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API_ERROR", "에러 본문 읽기 실패", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_ERROR", "네트워크 오류 발생: " + t.getMessage(), t);
                Toast.makeText(SignInActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}