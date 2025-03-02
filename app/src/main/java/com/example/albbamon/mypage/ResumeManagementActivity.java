package com.example.albbamon.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.network.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import okhttp3.ResponseBody;
import retrofit2.Call;

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

        ResumeAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        Call call = apiService.selectResumeMobile();




        LayoutInflater inflater = LayoutInflater.from(this);
        View resumeCard = inflater.inflate(R.layout.resume_card, containerLayout, false);
        containerLayout.addView(resumeCard);

        // btn_more를 각 카드에서 개별적으로 찾기
        ImageView btnMore = resumeCard.findViewById(R.id.btn_more);

        if (btnMore != null) {
            btnMore.setOnClickListener(v -> showBottomSheetDialog());
        } else {
            Log.e("ResumeManagement", "btn_more를 찾을 수 없습니다.");
        }
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