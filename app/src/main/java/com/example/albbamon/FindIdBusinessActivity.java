package com.example.albbamon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.albbamon.model.UserModel;
import com.example.albbamon.network.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindIdBusinessActivity extends AppCompatActivity {

    private Button btnPersonal, btnBusiness, btnBizNext;
    private View viewIndicatorPersonal, viewIndicatorBusiness;
    private RadioGroup radioGroupFindMethodBiz;
    private LinearLayout layoutBizNumber, layoutBizEmail, layoutBizCert;
    private EditText etBizName, etBizNumber, etBizEmail;

    // 결과 영역 관련 뷰들 (개인회원과 유사)
    private LinearLayout layoutFindResult;
    private TextView tvFindResultMessage, tvFindResultGuide, tvFoundId;
    private Button btnFindResultLogin, btnFindResultPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_business); // XML 파일명에 맞춰주세요.

        // 1. 기본 탭 및 인디케이터 연결
        btnPersonal = findViewById(R.id.btnPersonal);
        btnBusiness = findViewById(R.id.btnBusiness);
        viewIndicatorPersonal = findViewById(R.id.viewIndicatorPersonal);
        viewIndicatorBusiness = findViewById(R.id.viewIndicatorBusiness);

        // 2. RadioGroup 및 입력 레이아웃 연결
        radioGroupFindMethodBiz = findViewById(R.id.radioGroupFindMethodBiz);
        layoutBizNumber = findViewById(R.id.layoutBizNumber);
        layoutBizEmail = findViewById(R.id.layoutBizEmail);
        layoutBizCert = findViewById(R.id.layoutBizCert);

        etBizName = findViewById(R.id.etBizName);
        etBizNumber = findViewById(R.id.etBizNumber);
        etBizEmail = findViewById(R.id.etBizEmail);

        btnBizNext = findViewById(R.id.btnBizNext);

        // 3. 결과 영역 뷰 연결
        layoutFindResult = findViewById(R.id.layoutFindResult);
        tvFindResultMessage = findViewById(R.id.tvFindResultMessage);
        tvFindResultGuide = findViewById(R.id.tvFindResultGuide);
        tvFoundId = findViewById(R.id.tvFoundId);
        btnFindResultLogin = findViewById(R.id.btnFindResultLogin);
        btnFindResultPw = findViewById(R.id.btnFindResultPw);

        // 결과 영역 초기 숨김 처리
        layoutFindResult.setVisibility(View.GONE);

        // 4. 탭 버튼 이벤트
        btnPersonal.setOnClickListener(v -> {
            // 개인회원 화면으로 전환
            startActivity(new Intent(this, FindIdPersonalActivity.class));
            finish();
        });
        btnBusiness.setOnClickListener(v -> {
            // 현재 기업회원 화면이므로 인디케이터 활성화만 처리
            activateBusinessTab();
        });

        // 5. RadioGroup 선택 변경 리스너
        radioGroupFindMethodBiz.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioBizNumber) {
                layoutBizNumber.setVisibility(View.VISIBLE);
                layoutBizEmail.setVisibility(View.GONE);
                layoutBizCert.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioBizEmail) {
                layoutBizNumber.setVisibility(View.GONE);
                layoutBizEmail.setVisibility(View.VISIBLE);
                layoutBizCert.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioBizCert) {
                layoutBizNumber.setVisibility(View.GONE);
                layoutBizEmail.setVisibility(View.GONE);
                layoutBizCert.setVisibility(View.VISIBLE);
            }
        });

        // 6. "다음" 버튼 클릭 시 API 호출 (입력값에 따라 파라미터 구성)
        btnBizNext.setOnClickListener(v -> {
            int checked = radioGroupFindMethodBiz.getCheckedRadioButtonId();
            if (checked == R.id.radioBizNumber) {
                String bizName = etBizName.getText().toString().trim();
                String bizNumber = etBizNumber.getText().toString().trim();
                callFindIdApi(bizName, bizNumber, "");
            } else if (checked == R.id.radioBizEmail) {
                String email = etBizEmail.getText().toString().trim();
                callFindIdApi("", "", email);
            } else if (checked == R.id.radioBizCert) {
                // 본인인증 방식 처리 (추후 구현)
                Toast.makeText(this, "본인인증 기능을 구현하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 7. 결과 영역 내 버튼 클릭 이벤트
        btnFindResultLogin.setOnClickListener(v -> {
            // 로그인 화면 이동 등 로직 구현
            // startActivity(new Intent(this, LoginActivity.class));
        });
        btnFindResultPw.setOnClickListener(v -> {
            // 비밀번호 찾기 화면 이동 등 로직 구현
            // startActivity(new Intent(this, FindPwActivity.class));
        });
    }

    private void activateBusinessTab() {
        // 기업회원 탭 활성화: 개인회원 인디케이터는 회색, 기업회원 인디케이터는 빨간색
        viewIndicatorPersonal.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        viewIndicatorBusiness.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
    }

    private void callFindIdApi(String bizName, String bizNumber, String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:60085")  // 실제 API URL로 변경
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApiService apiService = retrofit.create(UserApiService.class);
        // 아래 메서드는 예시입니다. 실제 API 엔드포인트 및 파라미터 구조에 맞게 구현하세요.
        Call<List<UserModel>> call = apiService.findUserId(bizName, bizNumber, email);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserModel> userList = response.body();
                    if (!userList.isEmpty()) {
                        // 안내 문구 업데이트
                        String infoMsg = "입력하신 정보와 일치하는 " + userList.size() + "개의 아이디가 있습니다.";
                        tvFindResultMessage.setText(infoMsg);

                        // 여러 결과를 하나의 문자열로 구성 (각 이메일 앞에 번호 붙임)
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < userList.size(); i++) {
                            UserModel user = userList.get(i);
                            if (user.getData() != null && user.getData().getUserInfo() != null) {
                                String userEmail = user.getData().getUserInfo().getEmail();
                                sb.append((i + 1)).append(") ").append(userEmail).append("\n");
                            }
                        }
                        tvFoundId.setText(sb.toString());
                        layoutFindResult.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(FindIdBusinessActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FindIdBusinessActivity.this, "아이디 찾기 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(FindIdBusinessActivity.this, "API 호출 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
