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
import android.widget.RadioGroup; // [ADD]
import android.widget.TextView;
import android.widget.Toast;

import com.example.albbamon.model.UserFindIdModel;
import com.example.albbamon.network.UserApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindIdPersonalActivity extends AppCompatActivity {

    private Button btnPersonal, btnBusiness, btnNext;
    private View viewIndicatorPersonal, viewIndicatorBusiness;
    private LinearLayout layoutFindResult;
    private TextView tvFoundId;
    private EditText etName, etPhone;

    // XML에 정의된 ID에 맞게 변수 선언 (안내 문구 및 버튼들)
    private TextView tvFindResultMessage, tvFindResultGuide;
    private Button btnFindResultLogin, btnFindResultPw;

    // [ADD] ------------------------ 추가할 변수들 ------------------------
    private RadioGroup radioGroupFindMethod;
    private LinearLayout layoutContact, layoutBizEmail, layoutCert;
    private EditText etBizEmail;
    // [ADD] -----------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_personal);

        // 기존 뷰 연결
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnPersonal = findViewById(R.id.btnPersonal);
        btnBusiness = findViewById(R.id.btnBusiness);
        btnNext = findViewById(R.id.btnNext);
        viewIndicatorPersonal = findViewById(R.id.viewIndicatorPersonal);
        viewIndicatorBusiness = findViewById(R.id.viewIndicatorBusiness);
        layoutFindResult = findViewById(R.id.layoutFindResult);
        tvFoundId = findViewById(R.id.tvFoundId);

        tvFindResultMessage = findViewById(R.id.tvFindResultMessage);
        tvFindResultGuide = findViewById(R.id.tvFindResultGuide);
        btnFindResultLogin = findViewById(R.id.btnFindResultLogin);
        btnFindResultPw = findViewById(R.id.btnFindResultPw);

        // 초기 상태: 결과 영역 숨김
        layoutFindResult.setVisibility(View.GONE);

        // 탭 버튼 이벤트
        btnPersonal.setOnClickListener(v -> activatePersonalTab());
        btnBusiness.setOnClickListener(v -> {
            startActivity(new Intent(this, FindIdBusinessActivity.class));
            finish();
        });

        // "다음" 버튼 클릭 시 API 호출
        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String ceoNum = "";  // 개인회원이므로 빈 문자열 사용

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "이름과 전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            callFindIdApi(name, phone, ceoNum);
        });

        // 로그인 버튼 클릭
        btnFindResultLogin.setOnClickListener(v -> {
            // 예: 로그인 화면으로 이동
        });

        // 비밀번호 찾기 버튼 클릭
        btnFindResultPw.setOnClickListener(v -> {
            // 예: 비밀번호 찾기 화면으로 이동
        });

        // [ADD] --------------------- 추가 로직 시작 ---------------------

        // 1) RadioGroup과 세 레이아웃, 이메일 EditText 연결
        radioGroupFindMethod = findViewById(R.id.radioGroupFindMethod);
        layoutContact       = findViewById(R.id.layoutContact);
        layoutBizEmail      = findViewById(R.id.layoutBizEmail);
        layoutCert          = findViewById(R.id.layoutCert);
        etBizEmail          = findViewById(R.id.etBizEmail);

        // 2) 라디오 그룹 체크 변경 시 -> 해당 레이아웃만 보이기
        radioGroupFindMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioContact) {
                layoutContact.setVisibility(View.VISIBLE);
                layoutBizEmail.setVisibility(View.GONE);
                layoutCert.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioEmail) {
                layoutContact.setVisibility(View.GONE);
                layoutBizEmail.setVisibility(View.VISIBLE);
                layoutCert.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioCert) {
                layoutContact.setVisibility(View.GONE);
                layoutBizEmail.setVisibility(View.GONE);
                layoutCert.setVisibility(View.VISIBLE);
            }
        });
        // [ADD] ---------------------- 추가 로직 끝 ----------------------
    }

    private void activatePersonalTab() {
        viewIndicatorPersonal.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_red_light));
        viewIndicatorBusiness.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.darker_gray));
    }

    private void callFindIdApi(String name, String phone, String ceoNum) {
        // 기존 API 호출 로직 (수정 없음)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:60085")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApiService apiService = retrofit.create(UserApiService.class);
        Call<List<UserFindIdModel>> call = apiService.findUserId(name, phone, ceoNum);

        call.enqueue(new Callback<List<UserFindIdModel>>() {
            @Override
            public void onResponse(Call<List<UserFindIdModel>> call, Response<List<UserFindIdModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserFindIdModel> userList = response.body();
                    if (!userList.isEmpty()) {
                        // ✅ 아이디 찾기 성공
                        tvFoundId.setText(userList.get(0).getEmail());
                        layoutFindResult.setVisibility(View.VISIBLE);
                    } else {
                        // ✅ 아이디를 찾지 못한 경우
                        Toast.makeText(FindIdPersonalActivity.this, "입력하신 정보로 등록된 아이디가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // **여기에서 응답 상태 코드와 에러 메시지 출력**
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("API_ERROR", "서버 오류: " + response.code() + ", 메시지: " + errorMessage);
                        Toast.makeText(FindIdPersonalActivity.this, "서버 오류 발생! (에러 코드: " + response.code() + ")", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API_ERROR", "에러 메시지를 읽을 수 없습니다.", e);
                        Toast.makeText(FindIdPersonalActivity.this, "서버 오류 발생!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserFindIdModel>> call, Throwable t) {
                Log.e("API_ERROR", "네트워크 오류: " + t.getMessage(), t);
                Toast.makeText(FindIdPersonalActivity.this, "네트워크 오류!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    }