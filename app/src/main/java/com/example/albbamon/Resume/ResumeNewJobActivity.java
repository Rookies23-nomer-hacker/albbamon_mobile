package com.example.albbamon.Resume;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private int totalPages = 1;  // 총 페이지 개수 (기본값: 1)
    private LinearLayout paginationLayout; // 동적으로 버튼을 추가할 레이아웃
    private int currentPage = 1; // ✅ 현재 페이지 번호 (0부터 시작)
    private final int pageSize = 10; // ✅ 한 페이지당 아이템 개수

    private String selectedJobTitle = "채용 공고"; // 기본값 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        recyclerJobList = findViewById(R.id.recyclerJobList);
        recyclerJobList.setLayoutManager(new LinearLayoutManager(this));

        tvTitle = findViewById(R.id.tvTitle);
        paginationLayout = findViewById(R.id.paginationLayout);
        if (paginationLayout == null) {
            Log.e("DEBUG", "paginationLayout이 XML에서 제대로 초기화되지 않았습니다!");
        }

        jobList = new ArrayList<>();
        jobAdapter2 = new JobAdapter2(this, jobList);
        recyclerJobList.setAdapter(jobAdapter2);

        fetchRecruitmentPosts(); // 초기 데이터 로드

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
        Call<RecruitmentResponse> call = recruitmentAPI.getRecruitmentPosts(currentPage, pageSize); // ✅ API에는 0부터 전달

        call.enqueue(new Callback<RecruitmentResponse>() {
            @Override
            public void onResponse(Call<RecruitmentResponse> call, Response<RecruitmentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecruitmentResponse recruitmentResponse = response.body();
                    jobList.clear();

                    if (recruitmentResponse.getData() != null && recruitmentResponse.getData().getRecruitmentList() != null) {
                        List<RecruitmentModel> newJobs = recruitmentResponse.getData().getRecruitmentList();

                        jobList.addAll(newJobs);
                    }

                    if (recruitmentResponse.getData().getPageInfo() != null) {
                        totalPages = recruitmentResponse.getData().getPageInfo().getTotalPages();
                    } else {
                        totalPages = 3;
                    }

                    if (totalPages < 1) {
                        totalPages = 1;
                    }

                    jobAdapter2.notifyDataSetChanged();
                    createPaginationButtons();
                } else {
                    Toast.makeText(ResumeNewJobActivity.this, "채용 공고 데이터 로딩 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecruitmentResponse> call, Throwable t) {
                Toast.makeText(ResumeNewJobActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPaginationButtons() {
        paginationLayout.removeAllViews(); // 기존 버튼 제거

        int totalGroups = (int) Math.ceil((double) totalPages / 5); // 전체 그룹 수 계산
        int currentGroup = (currentPage - 1) / 5; // 현재 그룹 계산 (0부터 시작)

        int startPage = currentGroup * 5 + 1;
        int endPage = Math.min(startPage + 4, totalPages); // 5개까지만 표시

        // ✅ "이전" 버튼 추가 (첫 번째 그룹에서는 비활성화)
        if (currentGroup > 0) {
            Button prevButton = new Button(this);
            prevButton.setText("이전");
            prevButton.setTextSize(14);
            prevButton.setPadding(16, 8, 16, 8);
            prevButton.setBackgroundColor(getResources().getColor(R.color.gray));
            prevButton.setTextColor(getResources().getColor(R.color.black));

            prevButton.setOnClickListener(v -> {
                currentPage = startPage - 1; // 이전 그룹의 마지막 페이지로 이동
                fetchRecruitmentPosts();
            });

            paginationLayout.addView(prevButton);
        }

        // ✅ 페이지 버튼 (1~5 또는 6~10 등 그룹 내 5개 버튼)
        for (int i = startPage; i <= endPage; i++) {
            Button pageButton = new Button(this);
            pageButton.setText(String.valueOf(i));
            pageButton.setTextSize(14);
            pageButton.setPadding(16, 8, 16, 8);

            if (i == currentPage) {
                pageButton.setBackgroundColor(getResources().getColor(R.color.gray));
                pageButton.setTextColor(getResources().getColor(R.color.white));
            } else {
                pageButton.setBackgroundColor(getResources().getColor(R.color.gray));
                pageButton.setTextColor(getResources().getColor(R.color.black));
            }

            final int selectedPage = i;
            pageButton.setOnClickListener(v -> {
                if (currentPage != selectedPage) {
                    currentPage = selectedPage;
                    fetchRecruitmentPosts();
                }
            });

            paginationLayout.addView(pageButton);
        }

        // ✅ "다음" 버튼 추가 (마지막 그룹에서는 비활성화)
        if (endPage < totalPages) {
            Button nextButton = new Button(this);
            nextButton.setText("다음");
            nextButton.setTextSize(14);
            nextButton.setPadding(16, 8, 16, 8);
            nextButton.setBackgroundColor(getResources().getColor(R.color.gray));
            nextButton.setTextColor(getResources().getColor(R.color.black));

            nextButton.setOnClickListener(v -> {
                currentPage = endPage + 1; // 다음 그룹의 첫 번째 페이지로 이동
                fetchRecruitmentPosts();
            });

            paginationLayout.addView(nextButton);
        }
    }
}
