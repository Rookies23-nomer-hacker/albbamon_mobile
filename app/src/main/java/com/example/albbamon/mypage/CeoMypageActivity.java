package com.example.albbamon.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.albbamon.MainActivity;
import com.example.albbamon.R;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.repository.UserRepository;

public class CeoMypageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceo_mypage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // UI 요소 초기화
        ImageView profileImg = findViewById(R.id.profile_img);
        TextView userName = findViewById(R.id.user_name);

        ImageView closeButton = findViewById(R.id.close_button);
        LinearLayout userInfoRoute = findViewById(R.id.user_info_section);
        LinearLayout layoutApply = findViewById(R.id.layout_recruitment_management);
        LinearLayout resumeManagePost = findViewById(R.id.layout_recruitment_post);
        LinearLayout managementUser = findViewById(R.id.layout_mangement_user);
        LinearLayout searchUser = findViewById(R.id.layout_search_user);

        // UserRepository 초기화
        UserRepository userRepository = new UserRepository(this);

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
                    Glide.with(CeoMypageActivity.this)
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

        // ✅ 프로필 이미지 클릭 이벤트 추가
        profileImg.setOnClickListener(v -> {
            Toast.makeText(this, "프로필 이미지 클릭됨", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CeoMypageActivity.this, ProfileEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // X 버튼 클릭 시 MainActivity로 이동
        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(CeoMypageActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 회원정보
        userInfoRoute.setOnClickListener(v -> {
            Intent intent = new Intent(CeoMypageActivity.this, UserInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

//        resumeManagePost.setOnClickListener(v -> {
//            Intent intent = new Intent(CeoMypageActivity.this, ResumeManagementActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//        });
//
//        managementUser.setOnClickListener(v -> {
//            Intent intent = new Intent(CeoMypageActivity.this, ApplicationStatusActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//        });
//
//        searchUser.setOnClickListener(v -> {
//            Intent intent = new Intent(CeoMypageActivity.this, ApplicationStatusActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//        });
    }
}
