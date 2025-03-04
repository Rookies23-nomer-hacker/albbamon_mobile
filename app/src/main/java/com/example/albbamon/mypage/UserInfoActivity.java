package com.example.albbamon.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.MemberWithdrawalActivity;
import com.example.albbamon.R;
import com.example.albbamon.SignIn;
import com.example.albbamon.api.UserAPI;
import com.example.albbamon.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        LinearLayout EditUserInfoActivity = findViewById(R.id.edit_member_info);
        LinearLayout ChangePassword = findViewById(R.id.edit_password);
        LinearLayout MemberWithdrawalActivity = findViewById(R.id.Withdraw);
        Button btnLogout = findViewById(R.id.btn_logout);


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("회원정보");

        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, UserMypageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        EditUserInfoActivity.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, com.example.albbamon.mypage.EditUserInfoActivity.class);
            intent.putExtra("fragment", "edit_user_info"); // EditUserInfo Fragment 표시
            Log.d("UserInfoActivity", "Starting EditUserInfoActivity with fragment: change_password");

            startActivity(intent);
        });

        ChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
            intent.putExtra("fragment", "change_password"); // ChangePassword Fragment 표시
            Log.d("UserInfoActivity", "Starting EditUserInfoActivity with fragment: change_password");
            startActivity(intent);
        });

        MemberWithdrawalActivity.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, MemberWithdrawalActivity.class);
            intent.putExtra("fragment", "change_password"); // ChangePassword Fragment 표시
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        String sessionCookie = prefs.getString("cookie", null);

        if (sessionCookie == null) {
            Log.e("LOGOUT", "🚨 로그아웃 실패: 세션 쿠키 없음");
            return;
        }

        UserAPI apiService = RetrofitClient.getRetrofitInstance().create(UserAPI.class);

        // ✅ 로그아웃 API 요청에 JSESSIONID 포함
        Call<ResponseBody> call = apiService.signOut(sessionCookie);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("LOGOUT", "✅ 서버 로그아웃 성공");

                    // ✅ 서버에서 새로운 JSESSIONID를 발급했는지 확인
                    String newSessionCookie = response.headers().get("Set-Cookie");
                    if (newSessionCookie != null) {
                        Log.d("LOGOUT", "🚨 새로운 세션 쿠키 감지: " + newSessionCookie);
                    }

                    // ✅ SharedPreferences에서 세션 정보 완전히 삭제
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    // ✅ 자동 로그인 정보 삭제
                    SharedPreferences eCache = getSharedPreferences("ECACHE", MODE_PRIVATE);
                    SharedPreferences.Editor cacheEditor = eCache.edit();
                    cacheEditor.clear();
                    cacheEditor.apply();

                    // ✅ 로그아웃 후 로그인 화면 이동
                    Intent intent = new Intent(UserInfoActivity.this, SignIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LOGOUT", "🚨 서버 로그아웃 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("LOGOUT", "🚨 네트워크 오류: " + t.getMessage());
            }
        });
    }


}
