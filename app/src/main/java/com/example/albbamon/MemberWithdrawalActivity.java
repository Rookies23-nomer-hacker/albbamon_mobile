package com.example.albbamon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MemberWithdrawalActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private Button btnWithdraw;         // 첫 번째 탈퇴 버튼 (화면 하단)
    private LinearLayout secondSection; // 두 번째 섹션 (숨김 → 표시)
    private TextView tvUserId;
    private EditText etPassword;
    private EditText etReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_withdrawal);

        // 액션바 뒤로가기 버튼 표시 (업 버튼)
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 뷰 연결
        scrollView = findViewById(R.id.scrollView);
        CheckBox cbAgree = findViewById(R.id.cbAgree);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        Button btnCancel = findViewById(R.id.btnCancel);
        secondSection = findViewById(R.id.secondSection);
        tvUserId = findViewById(R.id.tvUserId);
        etPassword = findViewById(R.id.etPassword);
        etReason = findViewById(R.id.etReason);
        Button btnFinalWithdrawal = findViewById(R.id.btnFinalWithdrawal);
        TextView tvNotice = findViewById(R.id.tvNotice);

        // 두 번째 항목(item2) 클릭 시 MailSettingsActivity로 이동
        LinearLayout item2 = findViewById(R.id.item2);
        item2.setOnClickListener(v -> {
            Toast.makeText(this, "메일/문자 수신 설정 화면으로 이동", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MailSettingsActivity.class);
            startActivity(intent);
        });

        // 초기 상태: 첫 화면의 탈퇴 버튼은 체크박스 체크 전 비활성, 두 번째 섹션은 숨김
        btnWithdraw.setEnabled(false);
        secondSection.setVisibility(LinearLayout.GONE);

        // 체크박스 체크 시 첫 번째 탈퇴 버튼 활성화
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> btnWithdraw.setEnabled(isChecked));

        // 첫 번째 탈퇴 버튼 클릭 시 두 번째 섹션 표시 및 부드러운 스크롤 이동
        btnWithdraw.setOnClickListener(v -> {
            secondSection.setVisibility(LinearLayout.VISIBLE);
            scrollView.post(() -> scrollView.smoothScrollTo(0, secondSection.getTop()));
        });

        // 취소 버튼 클릭 시 화면 종료
        btnCancel.setOnClickListener(v -> finish());

        // 최종 탈퇴 버튼 클릭 시 입력값 확인 후 탈퇴 처리 (실제 로직은 추가 구현 필요)
        btnFinalWithdrawal.setOnClickListener(v -> {
            String userId = tvUserId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String reason = etReason.getText().toString().trim();

            if (userId.isEmpty()) {
                Toast.makeText(this, "아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            // 소셜 로그인인 경우 비밀번호 입력이 없을 수 있음
            // TODO: 비밀번호 검증 로직 추가
            if (reason.isEmpty()) {
                Toast.makeText(this, "탈퇴 사유를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: 서버 통신 등 실제 탈퇴 처리 로직 구현
            Toast.makeText(this,
                    "아이디: " + userId + "\n" +
                            "비밀번호: " + (password.isEmpty() ? "미입력" : password) + "\n" +
                            "탈퇴 사유: " + reason + "\n탈퇴 완료!",
                    Toast.LENGTH_SHORT).show();
            finish();
        });

        // 안내문 HTML 파싱 후 설정 (res/values/strings.xml에 정의한 notice_text 사용)
        String htmlNotice = getString(R.string.notice_text);
        tvNotice.setText(Html.fromHtml(htmlNotice, Html.FROM_HTML_MODE_LEGACY));
    }

    // 액션바 뒤로가기 버튼(업 버튼)을 누르면 현재 Activity 종료
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

