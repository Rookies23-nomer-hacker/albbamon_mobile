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
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.repository.UserRepository;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CeoMypageActivity extends AppCompatActivity {
    private ImageView profileImg;
    private TextView userName;
    private ResumeAPI resumeAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceo_mypage);

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

        fetchResumeDetails();

        // 프로필 이미지 클릭 이벤트 추가
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

        // 공고 관리 페이지
        userInfoRoute.setOnClickListener(v -> {
            Intent intent = new Intent(CeoMypageActivity.this, UserInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        resumeManagement.setOnClickListener(v -> {
            Intent intent = new Intent(CeoMypageActivity.this, MyRecruitmentListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        layoutApply.setOnClickListener(v -> {
            Intent intent = new Intent(CeoMypageActivity.this, ApplicationStatusActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
    // ✅ 이력서 상세 정보 가져와서 프로필 이미지 설정하기
    private void fetchResumeDetails() {
        resumeAPI.getResume().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> resumeData = response.body();
                    if (resumeData.containsKey("resume_img_url")) {
                        String profileImageUrl = (String) resumeData.get("resume_img_url");

                        // ✅ Glide를 이용하여 이미지 로드
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(CeoMypageActivity.this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.round_account_circle_24) // 기본 이미지 설정
                                    .error(R.drawable.round_account_circle_24) // 오류 발생 시 기본 이미지
                                    .into(profileImg);
                        }
                    }
                } else {
                    Log.e("UserMypage", "이력서 데이터를 불러오지 못함");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("UserMypage", "API 호출 실패: " + t.getMessage());
            }
        });
    }

}