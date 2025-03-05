package com.example.albbamon.mypage;

import static java.security.AccessController.getContext;

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
import com.example.albbamon.dto.response.ApplyCountResponse;
import com.example.albbamon.dto.response.ApplyCountResponse;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.network.SupportStatusService;
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
        TextView count_resume = findViewById(R.id.txt_resume_count);



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

//        이력서 개수 가져오기
        ResumeAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);
        Call <Map<String, Object>> call = apiService.getMyResume();

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> resumeData = response.body();
                    int size = (resumeData != null) ? resumeData.size() : 0; // 🔹 Null 체크

                    Log.d("count_resume", "응답 데이터 개수: " + size);
                    // 응답이 무조건 map 형태로 오고, 1개의 이력서만 저장 가능하기에 1로 하드코딩
                    if(size>0){
                        count_resume.setText("1");
                    }else{
                        count_resume.setText("0");
                    }


                } else {
                    Log.e("count_resume", "서버 응답 실패: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.d("count_resume", "응답 데이터 개수: 는 0개이자 서버오류");
//                count_resume.setText("0");
            }
        });



        SupportStatusService appService = RetrofitClient.getRetrofitInstanceWithSession(this).create(SupportStatusService.class);
        Call<ApplyCountResponse> call2 = appService.getMyApplyCount();

        call2.enqueue(new Callback<ApplyCountResponse>() {
            @Override
            public void onResponse(Call<ApplyCountResponse> call, Response<ApplyCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String count = response.body().getData();

                    TextView applyCount = findViewById(R.id.apply_count);
                    applyCount.setText(count);

                    // ✅ Intent를 이용해 지원현황 데이터 전달
                    layoutApply.setOnClickListener(v -> {
                        Intent intent = new Intent(UserMypageActivity.this, ApplicationStatusActivity.class);
                        intent.putExtra("apply_count", count); // 지원서 개수 전달
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    });

            }}

            @Override
            public void onFailure(Call<ApplyCountResponse> call, Throwable t) {

            }
        });

        fetchResumeDetails();

        // 프로필 이미지 클릭 이벤트 추가
        profileImg.setOnClickListener(v -> {
            Toast.makeText(this, "프로필 이미지 클릭됨", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserMypageActivity.this, ProfileEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // X 버튼 클릭 시 MainActivity로 이동
//        closeButton.setOnClickListener(v -> {
//            Intent intent = new Intent(UserMypageActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        });
        closeButton.setOnClickListener(v -> finish());

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
                            Glide.with(UserMypageActivity.this)
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