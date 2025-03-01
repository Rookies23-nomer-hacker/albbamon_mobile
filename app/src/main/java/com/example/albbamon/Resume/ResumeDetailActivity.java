package com.example.albbamon.Resume;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.dto.request.ResumeRequestDto;
import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.repository.UserRepository;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeDetailActivity extends AppCompatActivity {

    private int resumeId;
    private TextView nameText, phoneText, emailText;
    private TextView schoolContent, jobContent, optionContent, introContent, portfolioContent;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_detail);

        nameText = findViewById(R.id.Name);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);

        schoolContent = findViewById(R.id.schoolContent);
        jobContent = findViewById(R.id.jobContent);
        optionContent = findViewById(R.id.optionContent);
        introContent = findViewById(R.id.introContent);
        portfolioContent = findViewById(R.id.portfolioContent);


        findViewById(R.id.BackIcon).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        // UserRepository 초기화
        userRepository = new UserRepository(this);

        // Retrofit API 초기화
        ResumeAPI resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

// 로그인된 유저 ID 가져오기 (UserRepository 활용)
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                long userId = userInfo.getId();
                Log.d("ResumeDetailActivity", "현재 로그인한 사용자 ID: " + userId);

                // API 호출하여 이력서 데이터 가져오기
                Call<ResumeResponseDto> call = resumeAPI.getResume(userId);
                call.enqueue(new Callback<ResumeResponseDto>() {
                    @Override
                    public void onResponse(Call<ResumeResponseDto> call, Response<ResumeResponseDto> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // ✅ JSON 변환 없이 그대로 사용 (서버에서 UTF-8로 변환했으므로)
                            updateUI(response.body());
                        } else {
                            Log.e("ResumeDetailActivity", "서버 응답이 null입니다.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResumeResponseDto> call, Throwable t) {
                        Log.e("ResumeDetailActivity", "API 호출 실패: " + t.getMessage());
                    }
                });


            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ResumeDetailActivity", "사용자 정보 가져오기 실패: " + errorMessage);
            }
        });




    }

    private void updateUI(ResumeResponseDto resume) {
        Log.d("DEBUG", "📌 학력사항: " + resume.getSchool() + " " + resume.getStatus());
        Log.d("DEBUG", "📌 경력사항: " + resume.getPersonal());
        Log.d("DEBUG", "📌 희망근무조건: " +
                "근무지: " + resume.getWork_place_region() + " " + resume.getWork_place_city() + ", " +
                "업직종: " + resume.getIndustry_occupation() + ", " +
                "근무형태: " + resume.getEmploymentType() + ", " +
                "근무기간: " + resume.getWorking_period() + ", " +
                "근무일시: " + resume.getWorking_day());
        Log.d("DEBUG", "📌 자기소개: " + resume.getIntroduction());
        Log.d("DEBUG", "📌 포트폴리오: " + resume.getPortfolioName());

        schoolContent.setText(resume.getSchool() + " " + resume.getStatus());
        jobContent.setText(resume.getPersonal());
        optionContent.setText(
                "근무지: " + resume.getWork_place_region() + " " + resume.getWork_place_city() + "\n" +
                        "업직종: " + resume.getIndustry_occupation() + "\n" +
                        "근무형태: " + resume.getEmploymentType() + "\n" +
                        "근무기간: " + resume.getWorking_period() + "\n" +
                        "근무일시: " + resume.getWorking_day()
        );
        introContent.setText(resume.getIntroduction());
        portfolioContent.setText(resume.getPortfolioName());
    }



}
