package com.example.albbamon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.api.ResponseWrapper2;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.model.RecruitmentApplyRequest;
import com.example.albbamon.model.RecruitmentDetailResponse;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.model.ResumeModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitmentViewActivity extends AppCompatActivity {
    private TextView tvTitle, tvCompany, tvWage, tvContents;
    private Long jobId; // ✅ job_id 저장

    private long userId;
    private long resumeId;
    private ImageView back_img_btn;
    private ResumeAPI resumeAPI;
    private RecruitmentAPI recruitmentAPI;

    private Button applyButton;

    private ViewPager2 viewPager;
    private BannerAdapter2 bannerAdapter;
    private List<String> bannerImageUrls;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details);  // ✅ XML 적용

        // ✅ UI 요소 연결 (setContentView 이후에 실행)
        tvTitle = findViewById(R.id.tvTitle);
        //tvCompany = findViewById(R.id.tvCompany);
        tvWage = findViewById(R.id.tvWage);
        tvContents = findViewById(R.id.tvContents);

        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);
        recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);


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

        // ✅ onCreate()에서 viewPager 먼저 초기화
        viewPager = findViewById(R.id.banner_viewpager);
        bannerImageUrls = new ArrayList<>();
        bannerAdapter = new BannerAdapter2(this, bannerImageUrls);
        viewPager.setAdapter(bannerAdapter);  // ✅ 여기서 먼저 어댑터를 연결


        back_img_btn = findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(view -> finish());

        applyButton = findViewById(R.id.apply_button);



        applyButton.setOnClickListener(v -> applyForJob());
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
                    // ✅ 채용 공고명 가져오기
                    String jobTitle = tvTitle.getText().toString();

                    // ✅ 지원 성공 후 ApplySuccessActivity로 이동
                    Intent intent = new Intent(RecruitmentViewActivity.this, ApplySuccessActivity.class);
                    intent.putExtra("job_title", jobTitle);
                    startActivity(intent);
                    finish();
                } else {
                    showErrorDialog("지원에 실패했습니다. 다시 시도해주세요.");
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper2<Void>> call, Throwable t) {
                showErrorDialog("네트워크 오류가 발생했습니다. 인터넷 연결을 확인해주세요.");
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



    private void fetchRecruitmentDetails(Long jobId) {
        Call<RecruitmentDetailResponse> call = recruitmentAPI.getRecruitmentDetails(jobId);

        call.enqueue(new Callback<RecruitmentDetailResponse>() {
            @Override
            public void onResponse(Call<RecruitmentDetailResponse> call, Response<RecruitmentDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecruitmentModel job = response.body().getData();

                    if (job != null) {
                        tvTitle.setText(job.getTitle());
                        tvWage.setText("시급: " + job.getWage() + "원");
                        tvContents.setText(job.getContents() != null ? job.getContents() : "상세 내용 없음");

                        // ✅ 모집공고 이미지 추가
                        bannerImageUrls.clear();
                        if (job.getFile() != null && !job.getFile().isEmpty()) {
                            bannerImageUrls.add(job.getFile()); // ✅ API에서 불러온 이미지 추가
                        } else {
                            bannerImageUrls.add("android.resource://" + getPackageName() + "/" + R.drawable.img_alrimi); // ✅ 기본 이미지 추가
                        }
                        bannerAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(RecruitmentViewActivity.this, "공고 데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecruitmentDetailResponse> call, Throwable t) {
                Toast.makeText(RecruitmentViewActivity.this, "네트워크 오류 발생!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
