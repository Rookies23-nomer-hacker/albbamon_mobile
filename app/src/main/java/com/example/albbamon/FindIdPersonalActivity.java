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

public class FindIdPersonalActivity extends AppCompatActivity {

    private Button btnPersonal, btnBusiness, btnNext;
    private View viewIndicatorPersonal, viewIndicatorBusiness;
    private LinearLayout layoutFindResult;
    private TextView tvFoundId;
    private EditText etName, etPhone;

    // XML에 정의된 ID에 맞게 변수 선언 (안내 문구 및 버튼들)
    private TextView tvFindResultMessage, tvFindResultGuide;
    private Button btnFindResultLogin, btnFindResultPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_personal);

        // 기본 뷰 연결
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnPersonal = findViewById(R.id.btnPersonal);
        btnBusiness = findViewById(R.id.btnBusiness);
        btnNext = findViewById(R.id.btnNext);
        viewIndicatorPersonal = findViewById(R.id.viewIndicatorPersonal);
        viewIndicatorBusiness = findViewById(R.id.viewIndicatorBusiness);
        layoutFindResult = findViewById(R.id.layoutFindResult);
        tvFoundId = findViewById(R.id.tvFoundId);

        // 추가된 뷰 연결 (XML에 미리 정의되어 있음)
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
            callFindIdApi(name, phone, ceoNum);
        });

        // 로그인 버튼 클릭
        btnFindResultLogin.setOnClickListener(v -> {
            // 예: 로그인 화면으로 이동
            // startActivity(new Intent(this, LoginActivity.class));
        });

        // 비밀번호 찾기 버튼 클릭
        btnFindResultPw.setOnClickListener(v -> {
            // 예: 비밀번호 찾기 화면으로 이동
            // startActivity(new Intent(this, FindPwActivity.class));
        });
    }

    private void activatePersonalTab() {
        viewIndicatorPersonal.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_red_light));
        viewIndicatorBusiness.setBackgroundColor(
                ContextCompat.getColor(this, android.R.color.darker_gray));
    }

    private void callFindIdApi(String name, String phone, String ceoNum) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:60085")  // 실제 API URL로 변경
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApiService apiService = retrofit.create(UserApiService.class);
        Call<List<UserModel>> call = apiService.findUserId(name, phone, ceoNum);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserModel> userList = response.body();
                    Log.d("usermodel111", "onResponse: " + userList.get(0));
                    if (!userList.isEmpty()) {
                        // 안내 문구 업데이트 (XML의 tvFindResultMessage)
                        String infoMessage = "입력하신 정보와 일치하는 " + userList.size() + "개의 아이디가 있습니다.";
                        tvFindResultMessage.setText(infoMessage);

                        for (UserModel model : userList){
                            Log.d("usermodel get1", "onResponse: "+model.getData());
                            Log.d("usermodel get2", "onResponse: "+model.getMessage());
                            Log.d("usermodel get3", "onResponse: "+model.getStatus());
                            Log.d("usermodel get4", "onResponse: "+model);
                        }

                        // (선택) 추가 안내 가이드 업데이트 (XML의 tvFindResultGuide)
                        // tvFindResultGuide.setText("개인정보 보호를 위해 일부 정보는 마스킹 처리됩니다.\n회원 가입 시 본인인증을 한 경우, 추가 본인인증을 통해 전체 아이디를 확인하실 수 있습니다.");

                        // 아이디 목록 구성
                        StringBuilder resultBuilder = new StringBuilder();
                        resultBuilder.append("총 ").append(userList.size()).append("개의 결과 발견\n\n");
                        for (int i = 0; i < userList.size(); i++) {
                            UserModel user = userList.get(i);
                            if (user.getData() != null && user.getData().getUserInfo() != null) {
                                // 1) 올바른 이메일 가져오기
                                String email = user.getData().getUserInfo().getEmail();

                                // 2) resultBuilder에 추가
                                resultBuilder.append(i + 1)
                                        .append(") ")
                                        .append(email)
                                        .append("\n");
                            }
                        }

                        tvFoundId.setText(resultBuilder.toString());
                        layoutFindResult.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(FindIdPersonalActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FindIdPersonalActivity.this, "아이디 찾기 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Toast.makeText(FindIdPersonalActivity.this, "API 호출 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
