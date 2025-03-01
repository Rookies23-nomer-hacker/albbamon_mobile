package com.example.albbamon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.mypage.UserMypageActivity;

public class ApplySuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_success);

        // ✅ 채용 공고명 가져오기
        String jobTitle = getIntent().getStringExtra("job_title");
        TextView companyNameTextView = findViewById(R.id.company_name);
        if (jobTitle != null) {
            companyNameTextView.setText(jobTitle);
        }

        // ✅ "지원현황 확인" 버튼 클릭 시 -> 마이페이지 이동
        Button btnCheckStatus = findViewById(R.id.btn_check_status);
        btnCheckStatus.setOnClickListener(v -> {
            Intent intent = new Intent(ApplySuccessActivity.this, com.example.albbamon.mypage.UserMypageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 기존 스택 제거 후 이동
            startActivity(intent);
            finish();
        });

        // ✅ "메모 하기" 버튼 클릭 시 -> 메모 화면 이동 (추가 기능 필요 시 변경)
        Button btnMemo = findViewById(R.id.btn_memo);
        btnMemo.setOnClickListener(v -> {
            Intent intent = new Intent(ApplySuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 기존 스택 제거 후 이동
            startActivity(intent);
            finish();
        });
    }
}
