package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.bumptech.glide.Glide;

public class UserMypage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mypage);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // UI 요소 초기화
        ImageView profileImg = findViewById(R.id.profile_img);
        TextView userName = findViewById(R.id.user_name);
        ImageView closeButton = findViewById(R.id.close_button);
        LinearLayout userInfo = findViewById(R.id.user_info_section);
        LinearLayout layoutApply = findViewById(R.id.layout_apply);


        // TODO: 백엔드에서 가져올 사용자 데이터 (MySQL 연동 후 수정)
        String userFullName = null;  // DB에서 가져온 이름 (없으면 null)
        String userProfileImageUrl = "";  // DB에서 가져온 프로필 이미지 URL

        // 사용자 이름 설정 (DB에서 가져오지 못하면 "이름없음")
        userName.setText((userFullName == null || userFullName.isEmpty()) ? "이름없음" : userFullName + "님");

        // 프로필 이미지 로드 (URL이 없을 경우 기본 이미지 적용)
        /*
        Glide.with(this)
                .load(userProfileImageUrl == null || userProfileImageUrl.isEmpty() ? R.drawable.round_account_circle_24 : userProfileImageUrl)
                .placeholder(R.drawable.round_account_circle_24)
                .error(R.drawable.round_account_circle_24)
                .circleCrop()
                .into(profileImg);
         */

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

        layoutApply.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypage.this, ApplicationStatus.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }
}
