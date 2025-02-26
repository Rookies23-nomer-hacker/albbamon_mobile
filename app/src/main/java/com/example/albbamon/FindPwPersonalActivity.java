package com.example.albbamon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPwPersonalActivity extends AppCompatActivity {

    private Button btnPersonal, btnBusiness, btnIssueTempPw;
    private View viewIndicatorPersonal, viewIndicatorBusiness;
    private EditText etUserId, etName, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_personal);

        // 탭 버튼 연결
        btnPersonal = findViewById(R.id.btnPersonal);
        btnBusiness = findViewById(R.id.btnBusiness);

        // 인디케이터 연결
        viewIndicatorPersonal = findViewById(R.id.viewIndicatorPersonal);
        viewIndicatorBusiness = findViewById(R.id.viewIndicatorBusiness);

        // 임시비밀번호 신청 레이아웃 내 위젯 연결
        etUserId = findViewById(R.id.etUserId);
        etName   = findViewById(R.id.etName);
        etPhone  = findViewById(R.id.etPhone);
        btnIssueTempPw = findViewById(R.id.btnIssueTempPw);

        // 초기 상태: 개인회원 탭 활성화
        activatePersonalTab();

        // 탭 버튼 클릭 이벤트
        btnPersonal.setOnClickListener(v -> {
            // 이미 개인회원 탭
            activatePersonalTab();
        });
        btnBusiness.setOnClickListener(v -> {
            // 기업회원 비밀번호 찾기 화면으로 이동 (예시)
            startActivity(new Intent(this, FindPwBusinessActivity.class));
            finish();
        });

        // 임시비밀번호 발급 버튼 클릭
        btnIssueTempPw.setOnClickListener(v -> {
            // TODO: 임시비밀번호 발급 로직
            // etUserId, etName, etPhone 값 확인 후 서버 요청
            Toast.makeText(this, "임시비밀번호 발급 버튼 클릭됨", Toast.LENGTH_SHORT).show();
        });
    }

    private void activatePersonalTab() {
        // 개인회원 탭 활성화: 왼쪽 빨간색, 오른쪽 회색
        viewIndicatorPersonal.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
        viewIndicatorBusiness.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
    }
}
