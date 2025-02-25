package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.albbamon.api.UserApi;
import com.example.albbamon.model.UserModel;
import com.example.albbamon.network.RetrofitClient;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // 회원가입 버튼 클릭 시 account 화면으로 이동
        Button btnPer = findViewById(R.id.signUpBtn);
        btnPer.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), account.class);
            startActivity(intent);
        });

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setEnabled(true);

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
        Log.d("LOGIN_INPUT", "이메일: " + email + ", 비밀번호: " + password);

        UserApi apiService = RetrofitClient.getRetrofitInstance().create(UserApi.class);
        UserModel.Login login = new UserModel.Login(email, password);

        // 서버에서 단순 문자열을 반환하므로 ResponseBody 사용
        Call<ResponseBody> call = apiService.signIn(login);

        // API 요청 로그
        Log.d("API_REQUEST", "로그인 API 요청 전송: " + new Gson().toJson(login));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 응답 상태 코드 로그
                Log.d("API_RESPONSE", "HTTP 응답 코드: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // 서버 응답을 문자열로 변환
                        String responseBodyString = response.body().string().trim();
                        Log.d("API_RESPONSE", "서버 응답 원본: " + responseBodyString);

                        // 숫자로 변환 (서버가 "123" 같은 문자열 반환 시)
                        long userId = Long.parseLong(responseBodyString);

                        Log.d("API_RESPONSE", "로그인 성공 - userId: " + userId);
                        Toast.makeText(SignIn.this, "로그인 성공! ID: " + userId, Toast.LENGTH_SHORT).show();
                        finish();

                    } catch (Exception e) {
                        Log.e("API_ERROR", "서버 응답 처리 실패", e);
                        Toast.makeText(SignIn.this, "서버 응답을 처리할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 로그인 실패 시 응답 바디 확인
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "에러 바디 읽기 실패", e);
                    }

                    Log.e("API_ERROR", "로그인 실패 - 코드: " + response.code() + ", 메시지: " + response.message());
                    Log.e("API_ERROR", "서버 응답 바디: " + errorBody);

                    Toast.makeText(SignIn.this, "로그인 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 네트워크 오류 로그 출력
                Log.e("API_ERROR", "네트워크 오류 발생: " + t.getMessage(), t);
                Toast.makeText(SignIn.this, "네트워크 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
