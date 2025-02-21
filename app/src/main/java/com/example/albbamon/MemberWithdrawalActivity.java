package com.example.albbamon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MemberWithdrawalActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private CheckBox cbAgree;
    private Button btnWithdrawal;       // 첫 번째 탈퇴하기 버튼
    private LinearLayout secondSection; // 두 번째 섹션(숨김 → 표시)
    private Button btnFinalWithdrawal;  // 두 번째 섹션의 최종 탈퇴하기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_withdrawal);

        scrollView = findViewById(R.id.scrollView);
        cbAgree = findViewById(R.id.cbAgree);
        btnWithdrawal = findViewById(R.id.btnWithdrawal);
        secondSection = findViewById(R.id.secondSection);
        btnFinalWithdrawal = findViewById(R.id.btnFinalWithdrawal);

        // 체크박스 체크 시 '탈퇴하기' 버튼 활성/비활성
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnWithdrawal.setEnabled(isChecked);
        });

        // 첫 번째 '탈퇴하기' 버튼 클릭 → 두 번째 섹션 보여주고, ScrollView로 스크롤 이동
        btnWithdrawal.setOnClickListener(v -> {
            secondSection.setVisibility(View.VISIBLE);

            // 부드럽게 스크롤 이동
            scrollView.post(() -> scrollView.smoothScrollTo(0, secondSection.getTop()));
        });

        // 두 번째 섹션의 최종 탈퇴하기 버튼
        btnFinalWithdrawal.setOnClickListener(v -> {
            // TODO: 실제 탈퇴 처리 로직(서버 통신 등)
            Toast.makeText(this, "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

