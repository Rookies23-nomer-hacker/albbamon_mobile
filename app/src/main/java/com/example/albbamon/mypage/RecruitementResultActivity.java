package com.example.albbamon.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.dto.request.ResumeRequestDto;
import com.example.albbamon.dto.request.UpdateApplyStatusRequestDto;
import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.network.SuccessResponse;
import com.example.albbamon.repository.UserRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitementResultActivity extends AppCompatActivity {

    private long recruitmentId;
    private long applyId;
    private TextView nameText, phoneText, emailText;
    private TextView schoolContent, jobContent, optionContent, introContent, portfolioContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitement_result);

        // Intent에서 recruitmentId, applyId 받아오기
        Intent intent = getIntent();
        recruitmentId = intent.getLongExtra("recruitmentId", 0L);
        applyId = intent.getLongExtra("applyId", 0L);

        Log.d("RecruiteResultActivity", "받은 recruitmentId: " + recruitmentId);
        Log.d("RecruiteResultActivity", "받은 applyId: " + applyId);

        // UI 요소 초기화
        nameText = findViewById(R.id.Name);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        schoolContent = findViewById(R.id.schoolContent);
        jobContent = findViewById(R.id.jobContent);
        optionContent = findViewById(R.id.optionContent);
        introContent = findViewById(R.id.introContent);
        portfolioContent = findViewById(R.id.portfolioContent);

        // 뒤로가기 버튼 설정
        findViewById(R.id.BackIcon).setOnClickListener(v -> finish());

        // 합격/불합격 버튼
        Button passButton = findViewById(R.id.passButton);
        Button failButton = findViewById(R.id.failButton);

        passButton.setOnClickListener(v -> updateApplyStatus("PASS"));
        failButton.setOnClickListener(v -> updateApplyStatus("FAIL"));

        // 이력서 정보 불러오기
        loadResumeData();
    }

    private void loadResumeData() {
        ResumeAPI resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        Log.d("RecruitementResultActivity", "📌 resume_id 요청: " + applyId);

        Call<Map<String, Object>> call = resumeAPI.getResumeById(applyId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> resume = response.body();
                    Log.d("RecruitementResultActivity", "✅ 이력서 데이터 수신: " + resume.toString());
                    updateUI(resume);
                } else {
                    Log.e("RecruitementResultActivity", "❌ 서버 응답 실패: " + response.code());
                    try {
                        Log.e("RecruitementResultActivity", "오류 메시지: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("RecruitementResultActivity", "🚨 API 호출 실패: " + t.getMessage());
            }
        });
    }


    private void updateUI(Map<String, Object> resume) {
        String school = (String) resume.get("school");
        String status = (String) resume.get("status");
        String introduction = (String) resume.get("introduction");
        String portfolioName = (String) resume.get("portfolioName");

        Log.d("DEBUG", "📌 학력사항: " + school + " " + status);
        Log.d("DEBUG", "📌 자기소개: " + introduction);
        Log.d("DEBUG", "📌 포트폴리오: " + portfolioName);

        schoolContent.setText(school + " " + status);
        introContent.setText(introduction);
        portfolioContent.setText(portfolioName);
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

    private void updateApplyStatus(String status) {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);

        // ✅ UpdateApplyStatusRequestDto 생성
        UpdateApplyStatusRequestDto requestDto = new UpdateApplyStatusRequestDto(status, "지원 상태 변경");

        recruitmentAPI.updateApplyStatus(recruitmentId, applyId, requestDto).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RecruitementResultActivity", "✅ 상태 변경 성공: " + response.body());
                    Toast.makeText(RecruitementResultActivity.this, "상태가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("RecruitementResultActivity", "❌ 상태 변경 실패");
                    Toast.makeText(RecruitementResultActivity.this, "변경 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("RecruitementResultActivity", "🚨 API 호출 오류: " + t.getMessage());
                Toast.makeText(RecruitementResultActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
