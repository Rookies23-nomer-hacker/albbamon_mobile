package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ResumeWriteActivity extends AppCompatActivity {

    private ImageView backIcon;
    private Button btnSave, btnEditProfile;
    private TextView addressText, phoneText, callText, emailText;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.resume_write);

        // UI 요소 연결
        backIcon = findViewById(R.id.BackIcon);
        btnSave = findViewById(R.id.btnSave);
        btnEditProfile = findViewById(R.id.userEdit);
        scrollView = findViewById(R.id.scrollView);

        addressText = findViewById(R.id.addressText);
        phoneText = findViewById(R.id.phoneText);
        callText = findViewById(R.id.callText);
        emailText = findViewById(R.id.emailText);

        // (선택) 데이터를 동적으로 설정할 경우
        setUserData();

        // 뒤로 가기 버튼 클릭 이벤트
        backIcon.setOnClickListener(v -> finish()); // 현재 액티비티 종료

        // 회원정보 수정 버튼 클릭 이벤트
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(ResumeWriteActivity.this, "회원정보 수정 화면으로 이동", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, UserEditActivity.class));  // 회원정보 수정 화면 이동 시 사용
        });

        // 이력서 저장 버튼 클릭 이벤트
        btnSave.setOnClickListener(v -> {
            Toast.makeText(ResumeWriteActivity.this, "이력서가 저장되었습니다!", Toast.LENGTH_SHORT).show();
        });

        // ScrollView를 맨 위로 이동
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));
    }

    // 사용자 데이터를 동적으로 설정하는 함수
    private void setUserData() {
        addressText.setText("서울 동작구 상도동");
        phoneText.setText("010-1234-5678");
        callText.setText("02-1234-5678");
        emailText.setText("user@example.com");
    }

    public void goToEducationPage(View view) {
//        Intent intent = new Intent(this, EducationActivity.class);
//        startActivity(intent);
        Toast.makeText(ResumeWriteActivity.this, "학력사항 개발중", Toast.LENGTH_SHORT).show();
        
    }
    public void goToCareerPage(View view) {
//        Intent intent = new Intent(this, CareerActivity.class);
//        startActivity(intent);
        Toast.makeText(ResumeWriteActivity.this, "경력사항 개발중", Toast.LENGTH_SHORT).show();
    }

    public void goToIntroPage(View view) {
        Toast.makeText(ResumeWriteActivity.this, "자기소개 개발중", Toast.LENGTH_SHORT).show();

    }
    public void goToPortfolioPage(View view) {
        Toast.makeText(ResumeWriteActivity.this, "포트폴리오 개발중", Toast.LENGTH_SHORT).show();

    }
}
