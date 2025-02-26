package com.example.albbamon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FindPwBusinessActivity extends AppCompatActivity {

    private Button btnPersonal, btnBusiness, btnBizPwNext;
    private View viewIndicatorPersonal, viewIndicatorBusiness;
    private EditText etBizInfo;  // 사업자등록번호 또는 가입자명을 입력하는 필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_business);

        // 탭 버튼 및 인디케이터 연결
        btnPersonal = findViewById(R.id.btnPersonal);
        btnBusiness = findViewById(R.id.btnBusiness);
        viewIndicatorPersonal = findViewById(R.id.viewIndicatorPersonal);
        viewIndicatorBusiness = findViewById(R.id.viewIndicatorBusiness);

        // 사업자 정보 입력 필드 연결
        etBizInfo = findViewById(R.id.etBizInfo);

        // 다음 버튼 연결
        btnBizPwNext = findViewById(R.id.btnBizPwNext);

        // 기업회원 탭 활성화
        activateBusinessTab();

        // 탭 버튼 클릭 이벤트
        btnPersonal.setOnClickListener(v -> {
            // 개인회원 비밀번호 찾기 화면으로 전환
            startActivity(new Intent(this, FindPwPersonalActivity.class));
            finish();
        });
        btnBusiness.setOnClickListener(v -> {
            // 이미 기업회원 화면이므로 인디케이터만 갱신
            activateBusinessTab();
        });

        // "다음" 버튼 클릭 이벤트: 입력 정보 확인 후 임시 비밀번호 발급 로직 실행
        btnBizPwNext.setOnClickListener(v -> {
            String info = etBizInfo.getText().toString().trim();
            if (info.isEmpty()) {
                Toast.makeText(this, "사업자등록번호 또는 가입자명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            // TODO: 서버 API 호출하여 임시 비밀번호 발급 로직 구현
            Toast.makeText(this, "임시 비밀번호 발급 (입력 정보: " + info + ")", Toast.LENGTH_SHORT).show();
        });
    }

    private void activateBusinessTab() {
        // 기업회원 탭 활성화: 개인회원 인디케이터는 회색, 기업회원 인디케이터는 빨간색
        viewIndicatorPersonal.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        viewIndicatorBusiness.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
    }
}
