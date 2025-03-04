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
import com.example.albbamon.dto.request.UpdateApplyStatusRequestDto;
import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.network.RetrofitClient;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
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

        Intent intent = getIntent();
        recruitmentId = intent.getLongExtra("recruitmentId", 0L);
        applyId = intent.getLongExtra("applyId", 0L);

        Log.d("RecruitementResultActivity", "받은 recruitmentId: " + recruitmentId);
        Log.d("RecruitementResultActivity", "받은 applyId: " + applyId);

        nameText = findViewById(R.id.Name);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        schoolContent = findViewById(R.id.schoolContent);
        jobContent = findViewById(R.id.jobContent);
        optionContent = findViewById(R.id.optionContent);
        introContent = findViewById(R.id.introContent);
        portfolioContent = findViewById(R.id.portfolioContent);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish());

        Button passButton = findViewById(R.id.passButton);
        Button failButton = findViewById(R.id.failButton);

        passButton.setOnClickListener(v -> updateApplyStatus("PASSED"));
        failButton.setOnClickListener(v -> updateApplyStatus("FAILED"));

        loadResumeData();
    }

    private void loadResumeData() {
        ResumeAPI resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);
        Call<Map<String, Object>> call = resumeAPI.getResumeById(applyId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> resume = response.body();
                    updateUI(resume);
                } else {
                    Log.e("RecruitementResultActivity", "❌ 서버 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("RecruitementResultActivity", "🚨 API 호출 실패: " + t.getMessage());
            }
        });
    }

    private void updateUI(Map<String, Object> resume) {
        schoolContent.setText((String) resume.get("school") + " " + resume.get("status"));
        introContent.setText((String) resume.get("introduction"));
        portfolioContent.setText((String) resume.get("portfolioName"));
    }

    private void updateApplyStatus(String status) {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);

        Log.d("RecruitementResultActivity", "📌 상태 변경 요청: status=" + status);

        UpdateApplyStatusRequestDto requestDto = new UpdateApplyStatusRequestDto(status);

        recruitmentAPI.updateApplyStatus(recruitmentId, applyId, requestDto).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // ✅ 응답을 JSON 객체로 파싱하여 처리
                        String responseString = response.body().string();
                        Log.d("RecruitementResultActivity", "✅ 상태 변경 성공: " + responseString);

                        Toast.makeText(RecruitementResultActivity.this, "상태가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("RecruitementResultActivity", "❌ 상태 변경 실패: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "없음";
                        Log.e("RecruitementResultActivity", "오류 메시지: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RecruitementResultActivity.this, "변경 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RecruitementResultActivity", "🚨 API 호출 오류: " + t.getMessage());
                Toast.makeText(RecruitementResultActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

}