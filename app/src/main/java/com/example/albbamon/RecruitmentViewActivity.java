package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.model.RecruitmentDetailResponse;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.network.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitmentViewActivity extends AppCompatActivity {
    private TextView tvTitle, tvCompany, tvWage, tvContents;
    private Long jobId; // ✅ job_id 저장

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details);  // ✅ XML 적용

        // ✅ UI 요소 연결 (setContentView 이후에 실행)
        tvTitle = findViewById(R.id.tvTitle);
        //tvCompany = findViewById(R.id.tvCompany);
        tvWage = findViewById(R.id.tvWage);
        tvContents = findViewById(R.id.tvContents);

        // ✅ Null 체크
        if (tvTitle == null || tvWage == null || tvContents == null) {
            Log.e("UI_ERROR", "findViewById()가 null을 반환했습니다. XML ID를 확인하세요.");
            return;
        }

        // ✅ Intent에서 job_id 가져오기
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("job_id")) {
            jobId = intent.getLongExtra("job_id", -1);
            Log.d("JOB_ID", "Received Job ID: " + jobId);  // ✅ 로그 확인

            if (jobId != -1) {
                fetchRecruitmentDetails(jobId);
            } else {
                Toast.makeText(this, "잘못된 공고 ID입니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("JOB_ID", "No job_id found in Intent");
        }

    }

    private void fetchRecruitmentDetails(Long jobId) {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);
        Call<RecruitmentDetailResponse> call = recruitmentAPI.getRecruitmentDetails(jobId);

        Log.d("API_REQUEST", "Fetching details for Job ID: " + jobId);

        call.enqueue(new Callback<RecruitmentDetailResponse>() {
            @Override
            public void onResponse(Call<RecruitmentDetailResponse> call, Response<RecruitmentDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecruitmentModel job = response.body().getData(); // ✅ "data" 내부의 단일 객체 가져오기
                    Log.d("API_RESPONSE", "Fetched Job: " + new Gson().toJson(job));

                    if (job != null) {
                        tvTitle.setText(job.getTitle());
                        //tvCompany.setText(job.getCompany() != null ? job.getCompany() : "회사명 없음");
                        tvWage.setText("시급: " + job.getWage() + "원");
                        tvContents.setText(job.getContents() != null ? job.getContents() : "상세 내용 없음");
                    } else {
                        Log.e("API_ERROR", "Response body is null");
                        Toast.makeText(RecruitmentViewActivity.this, "공고 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Response Failed. Code: " + response.code());
                    Log.e("API_ERROR", "Response Error Body: " + response.errorBody());
                    Toast.makeText(RecruitmentViewActivity.this, "공고 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecruitmentDetailResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(RecruitmentViewActivity.this, "네트워크 오류 발생!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
