package com.example.albbamon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FindPwBusinessActivity extends AppCompatActivity {

    private Button btnPersonal, btnBusiness, btnBizPwNext;
    private View viewIndicatorPersonal, viewIndicatorBusiness;
    private RadioGroup radioGroupPwMethodBiz;
    private LinearLayout layoutBizInfo, layoutBizCert;
    private EditText etBizInfo; // 입력란: 회원정보

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw_business);

        // 탭 버튼 연결
        btnPersonal = findViewById(R.id.btnPersonal);
        btnBusiness = findViewById(R.id.btnBusiness);

        // 인디케이터 연결
        viewIndicatorPersonal = findViewById(R.id.viewIndicatorPersonal);
        viewIndicatorBusiness = findViewById(R.id.viewIndicatorBusiness);

        // 라디오 그룹 및 레이아웃 연결 (기업회원용)
        radioGroupPwMethodBiz = findViewById(R.id.radioGroupPwMethodBiz);
        layoutBizInfo = findViewById(R.id.layoutBizInfo);
        layoutBizCert = findViewById(R.id.layoutBizCert);

        // 회원정보 입력란 연결
        etBizInfo = findViewById(R.id.etBizInfo);

        btnBizPwNext = findViewById(R.id.btnBizPwNext);

        // 초기 상태: 기업회원 탭 활성화
        activateBusinessTab();

        // 탭 버튼 클릭 이벤트
        btnPersonal.setOnClickListener(v -> {
            // 개인회원 비밀번호 찾기 화면으로 전환
            startActivity(new Intent(this, FindPwPersonalActivity.class));
            finish();
        });
        btnBusiness.setOnClickListener(v -> {
            // 이미 기업회원 탭이면 활성화만
            activateBusinessTab();
        });

        // 라디오 그룹 선택 변경 이벤트
        radioGroupPwMethodBiz.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioBizInfo) {
                layoutBizInfo.setVisibility(View.VISIBLE);
                layoutBizCert.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioBizCert) {
                layoutBizInfo.setVisibility(View.GONE);
                layoutBizCert.setVisibility(View.VISIBLE);
            }
        });

        // 다음 버튼 클릭 이벤트
        btnBizPwNext.setOnClickListener(v -> {
            if (layoutBizInfo.getVisibility() == View.VISIBLE) {
                String info = etBizInfo.getText().toString().trim();
                if (info.isEmpty()) {
                    Toast.makeText(this, "회원정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: 서버에 회원정보 기반 비밀번호 찾기 요청 처리
                    Toast.makeText(this, "임시비밀번호가 발급되었습니다.", Toast.LENGTH_SHORT).show();
                }
            } else if (layoutBizCert.getVisibility() == View.VISIBLE) {
                // TODO: 본인인증 로직 처리
                Toast.makeText(this, "본인인증 진행", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "옵션을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void activateBusinessTab() {
        // 기업회원 탭 활성화: 왼쪽 인디케이터 회색, 오른쪽 인디케이터 빨간색
        viewIndicatorPersonal.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        viewIndicatorBusiness.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
    }
}
