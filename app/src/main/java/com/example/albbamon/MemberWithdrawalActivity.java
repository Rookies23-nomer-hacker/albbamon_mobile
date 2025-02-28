package com.example.albbamon;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.albbamon.model.UserInfo;
import com.example.albbamon.repository.UserRepository;

public class MemberWithdrawalActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private Button btnWithdraw;
    private LinearLayout secondSection;
    private TextView tvUserId;
    private EditText etPassword;
    private EditText etReason;

    private long numericUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_withdrawal);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        findViewById(R.id.item2).setOnClickListener(v -> {
            Toast.makeText(this, "메일/문자 수신 설정 화면으로 이동", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MailSettingsActivity.class));
        });

        UserRepository userRepository = new UserRepository(this);
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                if (userInfo != null) {
                    tvUserId.setText(userInfo.getName() != null ? userInfo.getName() : "사용자 정보 없음");
                    numericUserId = userInfo.getId();
                    Log.d("UserMypage", "회원 정보 업데이트 성공: " + userInfo.getName() + ", id: " + numericUserId);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("UserMypage", "회원 정보 가져오기 실패: " + errorMessage);
            }
        });

        btnWithdraw.setEnabled(false);
        secondSection.setVisibility(LinearLayout.GONE);

        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> btnWithdraw.setEnabled(isChecked));

        btnWithdraw.setOnClickListener(v -> {
            secondSection.setVisibility(LinearLayout.VISIBLE);
            scrollView.post(() -> scrollView.smoothScrollTo(0, secondSection.getTop()));
        });

        btnCancel.setOnClickListener(v -> finish());

        btnFinalWithdrawal.setOnClickListener(v -> {
            String password = etPassword.getText().toString().trim();
            String reason = etReason.getText().toString().trim();

            if (numericUserId <= 0) {
                Toast.makeText(this, "유효한 사용자 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (reason.isEmpty()) {
                Toast.makeText(this, "탈퇴 사유를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            userRepository.deleteUser(new UserRepository.DeleteUserCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(MemberWithdrawalActivity.this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show();
                    // ✅ 회원 탈퇴 후 로그인 화면(SignIn)으로 이동
                    Intent intent = new Intent(MemberWithdrawalActivity.this, SignIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 기존 스택 삭제
                    startActivity(intent);
                    finish();
                }


                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(MemberWithdrawalActivity.this, "회원 탈퇴 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("회원탈퇴API", "Error: " + errorMessage);
                }
            });
        });

        String htmlNotice = getString(R.string.notice_text);
        tvNotice.setText(Html.fromHtml(htmlNotice, Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "뒤로가기 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}