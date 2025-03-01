package com.example.albbamon.Resume;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.ApplySuccessActivity;
import com.example.albbamon.JobAdapter2;
import com.example.albbamon.R;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.api.ResponseWrapper2;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeNewJobActivity extends AppCompatActivity {
    private RecyclerView recyclerJobList;
    private JobAdapter2 jobAdapter2;
    private List<RecruitmentModel> jobList;

    private TextView tvTitle;
    private Long jobId;
    private Button applyButton;
    private String selectedJobTitle = "채용 공고";  // 기본값 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        recyclerJobList = findViewById(R.id.recyclerJobList);
        recyclerJobList.setLayoutManager(new LinearLayoutManager(this));

        tvTitle = findViewById(R.id.tvTitle);

        jobList = new ArrayList<>();
        jobAdapter2 = new JobAdapter2(this, jobList);
        recyclerJobList.setAdapter(jobAdapter2);

        fetchRecruitmentPosts();

        // ✅ 지원하기 버튼 클릭 이벤트
        jobAdapter2.setOnItemClickListener(selectedJobId -> {
            jobId = selectedJobId;  // 선택한 공고 ID 저장

            // ✅ 선택한 jobId에 맞는 jobTitle을 찾아서 저장
            for (RecruitmentModel job : jobList) {
                if (job.getId().equals(jobId)) {
                    selectedJobTitle = job.getTitle();
                    tvTitle.setText(selectedJobTitle);
                    break;
                }
            }

            applyForJob();
        });
    }

    private void applyForJob() {
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);
        Call<ResponseWrapper2<Void>> call = recruitmentAPI.applyForJob(jobId, userId);

        call.enqueue(new Callback<ResponseWrapper2<Void>>() {
            @Override
            public void onResponse(Call<ResponseWrapper2<Void>> call, Response<ResponseWrapper2<Void>> response) {
                if (response.isSuccessful()) {
                    // ✅ 지원 성공 후 ApplySuccessActivity로 이동
                    Intent intent = new Intent(ResumeNewJobActivity.this, ApplySuccessActivity.class);
                    intent.putExtra("job_title", selectedJobTitle);
                    startActivity(intent);
                    finish();
                } else {
                    showErrorDialog("지원에 실패했습니다. 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper2<Void>> call, Throwable t) {
                Log.e("API_FAILURE", "네트워크 오류: " + t.getMessage());
                Toast.makeText(ResumeNewJobActivity.this, "네트워크 오류 발생!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("오류 발생")
                .setMessage(message)
                .setPositiveButton("확인", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void fetchRecruitmentPosts() {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);
        Call<RecruitmentResponse> call = recruitmentAPI.getRecruitmentPosts();

        call.enqueue(new Callback<RecruitmentResponse>() {
            @Override
            public void onResponse(Call<RecruitmentResponse> call, Response<RecruitmentResponse> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    RecruitmentResponse recruitmentResponse = response.body();
                    Log.d("API_RESPONSE", "Message: " + recruitmentResponse.getMessage());

                    jobList.clear();

                    if (recruitmentResponse.getData() != null && recruitmentResponse.getData().getRecruitmentList() != null) {
                        jobList.addAll(recruitmentResponse.getData().getRecruitmentList());
                    } else {
                        Log.e("API_ERROR", "recruitmentList가 비어 있음.");
                        Toast.makeText(ResumeNewJobActivity.this, "채용 공고 데이터 없음", Toast.LENGTH_SHORT).show();
                    }

                    jobAdapter2.notifyDataSetChanged();
                } else {
                    Log.e("API_ERROR", "Response Failed. Body: " + response.errorBody());
                    Toast.makeText(ResumeNewJobActivity.this, "채용 공고 데이터 로딩 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecruitmentResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(ResumeNewJobActivity.this, "채용 공고 API 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
