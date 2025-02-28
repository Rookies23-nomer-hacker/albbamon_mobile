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
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.repository.UserRepository;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMypageActivity extends AppCompatActivity {
    private ImageView profileImg;
    private TextView userName;
    private ResumeAPI resumeAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mypage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // UI 요소 초기화
        profileImg = findViewById(R.id.profile_img);
        userName = findViewById(R.id.user_name);

        ImageView closeButton = findViewById(R.id.close_button);
        LinearLayout userInfoRoute = findViewById(R.id.user_info_section);
        LinearLayout layoutApply = findViewById(R.id.layout_apply);
        LinearLayout resumeManagement = findViewById(R.id.layout_resume);

        // ✅ Retrofit 인스턴스 생성
        resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        // ✅ UserRepository 초기화
        UserRepository userRepository = new UserRepository(this);

        // ✅ fetchUserInfo() 호출하여 사용자 정보 가져오기
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                // 사용자 정보 출력
                userName.setText(userInfo.getName() != null ? userInfo.getName() : "이름 없음");

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("UserMypage", errorMessage);
            }
        });

        // ✅ 프로필 이미지 클릭 이벤트 추가
        profileImg.setOnClickListener(v -> {
            Toast.makeText(this, "프로필 이미지 클릭됨", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserMypageActivity.this, ProfileEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // X 버튼 클릭 시 MainActivity로 이동
        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypageActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        userInfoRoute.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypageActivity.this, UserInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        resumeManagement.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypageActivity.this, ResumeManagementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        layoutApply.setOnClickListener(v -> {
            Intent intent = new Intent(UserMypageActivity.this, ApplicationStatusActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}