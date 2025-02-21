package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.albbamon.api.UserAPI;
import com.example.albbamon.model.UserModel;
import com.example.albbamon.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMypage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mypage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // UI 요소 초기화
        ImageView profileImg = findViewById(R.id.profile_img);
        TextView userName = findViewById(R.id.user_name);

        ImageView closeButton = findViewById(R.id.close_button);
        LinearLayout userInfo = findViewById(R.id.user_info_section);
        LinearLayout layoutApply = findViewById(R.id.layout_apply);
        LinearLayout resumeManagement = findViewById(R.id.layout_resume);

        // Retrofit API 호출
        UserAPI userAPI = RetrofitClient.getRetrofitInstance().create(UserAPI.class);
        Call<UserModel> call = userAPI.getUserInfo(); // userId 없이 요청

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel user = response.body();
                UserModel.UserInfo userInfo = user.getData().getUserInfo();

                if (response.isSuccessful() && response.body() != null) {
                    // 이름설정
                    String name = userInfo.getName();
                    if (name == null || name.isEmpty()) {
                        name = "사용자 정보 없음";
                    }
                    userName.setText(name);

                    // 프로필 이미지 예외 처리 (없으면 기본 이미지)
                    String profileImgUrl = userInfo.getProfileImg();
                    if (profileImgUrl == null || profileImgUrl.isEmpty()) {
                        profileImg.setImageResource(R.drawable.round_account_circle_24);  // 기본 이미지 지정
                        Log.d("UserMypage", "🖼️ 기본 프로필 이미지 사용");
                    } else {
                        Glide.with(UserMypage.this)
                                .load(profileImgUrl)
                                .placeholder(R.drawable.round_account_circle_24)  // 로딩 중 기본 이미지
                                .error(R.drawable.round_account_circle_24)  // 오류 발생 시 기본 이미지
                                .into(profileImg);
                        Log.d("UserMypage", "🖼️ 프로필 이미지 로드: " + profileImgUrl);
                    }
                } else {
                    Log.e("UserMypage", "❌ 응답 실패: " + response.code());

                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("UserMypage", "❌ 응답 실패! 코드: " + response.code() + ", 오류 메시지: " + errorBody);
                    } catch (Exception e) {
                        Log.e("UserMypage", "❌ 응답 실패! 코드: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("UserMypage", "API 호출 실패", t);
            }
        });


        // X 버튼 클릭 시 MainActivity로 이동
        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypage.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        userInfo.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypage.this, UserInfo.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        resumeManagement.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypage.this, ResumeManagement.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        layoutApply.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypage.this, ApplicationStatus.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
