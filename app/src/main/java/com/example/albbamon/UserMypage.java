package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.repository.UserRepository;

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
        LinearLayout userInfoRoute = findViewById(R.id.user_info_section);
        LinearLayout layoutApply = findViewById(R.id.layout_apply);
        LinearLayout resumeManagement = findViewById(R.id.layout_resume);

        // UserRepository 초기화
        UserRepository userRepository;
        userRepository = new UserRepository();

        // ✅ fetchUserInfo() 호출하여 사용자 정보 가져오기
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                // ✅ 사용자 정보 출력
                userName.setText(userInfo.getName() != null ? userInfo.getName() : "이름 없음");

                // ✅ 프로필 이미지 예외 처리
                if (userInfo.getProfileImg() == null || userInfo.getProfileImg().isEmpty()) {
                    profileImg.setImageResource(R.drawable.round_account_circle_24);
                } else {
                    Glide.with(UserMypage.this)
                            .load(userInfo.getProfileImg())
                            .placeholder(R.drawable.round_account_circle_24)
                            .error(R.drawable.round_account_circle_24)
                            .into(profileImg);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("UserMypage", errorMessage);
            }
        });

        // X 버튼 클릭 시 MainActivity로 이동
        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypage.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        userInfoRoute.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypage.this, UserInfoActivity.class);
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
