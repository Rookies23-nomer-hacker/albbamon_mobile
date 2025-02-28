package com.example.albbamon.mypage;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.Experience.ExperienceUpdate;
import com.example.albbamon.Experience.ExperienceView;
import com.example.albbamon.R;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.network.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeManagementActivity extends AppCompatActivity {

    private LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_management);

        containerLayout = findViewById(R.id.container_layout);

        // 툴바 제목 및 뒤로가기 버튼
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("지원현황");

        ImageView backButton = findViewById(R.id.back);

        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        // 이력서 카드뷰 추가 (동적)
        addResumeCard();
    }

    private void addResumeCard() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View resumeCard = inflater.inflate(R.layout.resume_card, containerLayout, false);

        containerLayout.addView(resumeCard);
        TextView tv_address, tv_work_type, tv_career, createDate;
        tv_address = findViewById(R.id.tv_address);
        tv_work_type = findViewById(R.id.tv_work_type);
        tv_career = findViewById(R.id.tv_career);
        createDate = findViewById(R.id.createDate);

        // btn_more를 각 카드에서 개별적으로 찾기
        ImageView btnMore = resumeCard.findViewById(R.id.btn_more);

        //출력하기
        ResumeAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        SharedPreferences prefs = getSharedPreferences("SESSION", Context.MODE_PRIVATE);
        String savedCookie = prefs.getString("cookie", "");
        long userId = prefs.getLong("userId", -1);
        Log.d(TAG, "저장된 쿠키: " + savedCookie);
        Log.d(TAG, "저장된 userId: " + userId);

        Call<Map<String, Object>> call = apiService.getMyResume();
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> resumeData = response.body();
                    Log.d(TAG, "이력서 목록: " + resumeData.toString());
                    Toast.makeText(ResumeManagementActivity.this, "이력서 조회 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "이력서 조회 실패! 응답 코드: " + response.code());
                    Toast.makeText(ResumeManagementActivity.this, "이력서 조회 실패!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "API 호출 실패: " + t.getMessage());
                Toast.makeText(ResumeManagementActivity.this, "네트워크 오류 발생!", Toast.LENGTH_SHORT).show();
            }
        });

        //이력서 클릭하기
        FrameLayout frameButton = findViewById(R.id.frame_button);
        /*frameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResumeManagementActivity.this, ResumeDetailActivity.class);
//                intent.putExtra("postId", postId); // 이력서 아이디 넣어야됨
                startActivity(intent);
            }
        });
        if (btnMore != null) {
            btnMore.setOnClickListener(v -> showBottomSheetDialog());
        } else {
            Log.e("ResumeManagement", "btn_more를 찾을 수 없습니다.");
        }*/
    }

    private void showBottomSheetDialog() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_menu, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        LinearLayout btnDelete = bottomSheetView.findViewById(R.id.btn_delete);
        LinearLayout btnEmail = bottomSheetView.findViewById(R.id.btn_email);

        btnDelete.setOnClickListener(v -> {
            Toast.makeText(this, "이력서 삭제", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        btnEmail.setOnClickListener(v -> {
            Toast.makeText(this, "이메일로 전달", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}