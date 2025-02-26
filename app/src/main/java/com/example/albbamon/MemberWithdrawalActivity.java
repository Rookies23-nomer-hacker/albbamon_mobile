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

import com.example.albbamon.model.UserData;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.model.UserModel;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.network.SuccessResponse;
import com.example.albbamon.api.UserAPI;
import com.example.albbamon.repository.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberWithdrawalActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private Button btnWithdraw;
    private LinearLayout secondSection;
    private TextView tvUserId;
    private EditText etPassword;
    private EditText etReason;

    // 서버에서 받아온 실제 숫자형 사용자 아이디 (초기값: -1)
    private long numericUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_withdrawal);

        // 액션바 업 버튼 활성화
        if (getSupportActionBar() != null) {
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

        // 메일/문자수신 설정 화면 이동
        findViewById(R.id.item2).setOnClickListener(v -> {
            Toast.makeText(this, "메일/문자 수신 설정 화면으로 이동", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MailSettingsActivity.class));
        });

        // 사용자 정보 API 호출하여 업데이트
        UserRepository userRepository = new UserRepository(this);
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                numericUserId = userInfo.getId();
                tvUserId.setText(userInfo.getName() != null ? userInfo.getName() : "사용자 정보 없음");
                Log.d("MemberWithdrawal", "회원 정보 불러오기 성공: ID=" + numericUserId);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("MemberWithdrawal", "회원 정보 불러오기 실패: " + errorMessage);
            }
        });

//        UserAPI userAPI = RetrofitClient.getRetrofitInstanceWithoutSession().create(UserAPI.class);
//        Call<UserModel> call = userAPI.getUserInfo();
//        call.enqueue(new Callback<UserModel>() {
//            @Override
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    UserModel userModel = response.body();
//                    UserData userData = userModel.getData(); // UserData 가져오기
//
//                    if (userData != null && userData.getUserInfo() != null) {
//                        UserInfo userInfo = userData.getUserInfo(); // UserInfo 가져오기
//
//                        String name = userInfo.getName() != null ? userInfo.getName() : "사용자 정보 없음";
//                        numericUserId = userInfo.getId(); // getId() 접근
//
//                        tvUserId.setText(name);
//                        Log.d("UserMypage", "회원 정보 업데이트 성공: " + name + ", id: " + numericUserId);
//                    } else {
//                        Log.e("UserMypage", "유저 데이터가 null입니다.");
//                    }
//                } else {
//                    Log.e("UserMypage", "응답 실패: " + response.code());
//                    try {
//                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
//                        Log.e("UserMypage", "오류 메시지: " + errorBody);
//                    } catch (Exception e) {
//                        Log.e("UserMypage", "오류 처리 실패", e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                Log.e("UserMypage", "API 호출 오류", t);
//            }
//        });

        // 초기 상태: 탈퇴 버튼 비활성, 두 번째 섹션 숨김
        btnWithdraw.setEnabled(false);
        secondSection.setVisibility(LinearLayout.GONE);

        // 체크박스 선택 시 탈퇴 버튼 활성화
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> btnWithdraw.setEnabled(isChecked));

        // 탈퇴 버튼 클릭 시 두 번째 섹션 표시 및 스크롤 이동
        btnWithdraw.setOnClickListener(v -> {
            secondSection.setVisibility(LinearLayout.VISIBLE);
            scrollView.post(() -> scrollView.smoothScrollTo(0, secondSection.getTop()));
        });

        // 취소 버튼 클릭 시 화면 종료
        btnCancel.setOnClickListener(v -> finish());

        // 최종 탈퇴 버튼 클릭 시 API 호출
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

            // 회원 탈퇴 API 호출
            userRepository.deleteUser(this, numericUserId, new UserRepository.DeleteUserCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(MemberWithdrawalActivity.this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show();
                    Log.d("회원탈퇴", "회원 탈퇴 성공");
                    finish(); // 탈퇴 후 액티비티 종료
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(MemberWithdrawalActivity.this, "회원 탈퇴 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("회원탈퇴", "서버 응답 실패: " + errorMessage);
                }
            });


//            // 회원 탈퇴 API 호출
//            Call<SuccessResponse> callDel = userAPI.deleteUser(numericUserId);
//            callDel.enqueue(new Callback<SuccessResponse>() {
//                @Override
//                public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
//                    if (response.isSuccessful()) {
//                        Toast.makeText(MemberWithdrawalActivity.this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show();
//                        Log.e("회원탈퇴API", "회원 탈퇴 성공");
//                        finish();
//                    } else {
//                        Toast.makeText(MemberWithdrawalActivity.this, "회원 탈퇴 실패: " + response.message(), Toast.LENGTH_SHORT).show();
//                        Log.e("회원탈퇴API", "서버 응답 실패: " + response.code());
//                    }
//                }
//                @Override
//                public void onFailure(Call<SuccessResponse> call, Throwable t) {
//                    Toast.makeText(MemberWithdrawalActivity.this, "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.e("회원탈퇴API", "Error: " + t.getMessage());
//                    t.printStackTrace();
//                }
//            });
        });

        // 안내문 HTML 파싱 및 설정
        String htmlNotice = getString(R.string.notice_text);
        tvNotice.setText(Html.fromHtml(htmlNotice, Html.FROM_HTML_MODE_LEGACY));
    }

    // 액션바 업 버튼 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 물리적 뒤로가기 버튼 처리 (선택 사항)
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "뒤로가기 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
