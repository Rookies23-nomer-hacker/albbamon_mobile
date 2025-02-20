package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;

public class ResumePortfolioActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private LinearLayout layoutFileUpload, layoutUrl;

    // 파일 선택 요청 코드
    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_portfolio);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        // XML 요소 가져오기
        tabLayout = findViewById(R.id.tabLayout);
        layoutFileUpload = findViewById(R.id.layoutFileUpload);
        layoutUrl = findViewById(R.id.layoutUrl);

        // 탭 클릭 시 레이아웃 전환
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    layoutFileUpload.setVisibility(View.VISIBLE);
                    layoutUrl.setVisibility(View.GONE);
                } else {
                    layoutFileUpload.setVisibility(View.GONE);
                    layoutUrl.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // 파일 업로드 버튼 클릭 이벤트
        findViewById(R.id.btnFileUpload).setOnClickListener(v -> openFilePicker());


        // 저장 버튼
        findViewById(R.id.btnSave).setOnClickListener(v -> {
            Toast.makeText(ResumePortfolioActivity.this, "포트폴리오 저장완료", Toast.LENGTH_SHORT).show();
        });

    }

    // 파일 탐색기 열기
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // 모든 파일 유형 허용
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "파일 선택"), PICK_FILE_REQUEST);
    }

}
