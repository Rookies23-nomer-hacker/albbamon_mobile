package com.example.albbamon.mypage;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;
import com.example.albbamon.Resume.ResumeDetailActivity;
import com.example.albbamon.Resume.ResumeWriteActivity;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.network.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeManagementActivity extends AppCompatActivity {

    private LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_management);

        containerLayout = findViewById(R.id.container_layout);

        // 툴바 제목 및 뒤로가기 버튼
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("지원현황");

        ImageView backButton = findViewById(R.id.back);

        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        // 이력서 카드뷰 추가 (동적)
        addResumeCard();
    }

    private void addResumeCard() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View resumeCard = inflater.inflate(R.layout.resume_card, containerLayout, false);
        View resumeNoCard = inflater.inflate(R.layout.resume_nocard, containerLayout, false);



        // btn_more를 각 카드에서 개별적으로 찾기
        ImageView btnMore = resumeCard.findViewById(R.id.btn_more);

        //출력하기
        ResumeAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        SharedPreferences prefs = getSharedPreferences("SESSION", Context.MODE_PRIVATE);
        String savedCookie = prefs.getString("cookie", "");
        long userId = prefs.getLong("userId", -1);
        Log.d(TAG, "저장된 쿠키: " + savedCookie);
        Log.d(TAG, "저장된 userId: " + userId);

        Call<Map<String, Object>> call = apiService.getMyResume();
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
//                Log.d("deleteResume", String.valueOf(response));
//                Log.d("deleteResume", String.valueOf(response.code()));
//                Log.d("deleteResume", String.valueOf(response.body()));

                if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) {
                    Log.e("nocard", "이력서 없음 응답 코드: " + response.code());
                    Toast.makeText(ResumeManagementActivity.this, "이력서 없음", Toast.LENGTH_SHORT).show();
                    containerLayout.addView(resumeNoCard); // 이력서가 없을 경우 기본 화면 표시

                    Button writeResume = findViewById(R.id.btn_create_resume);
                    writeResume.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ResumeManagementActivity.this, ResumeWriteActivity.class);
                            startActivity(intent);
                        }
                    });
                    return;
                }

                Map<String, Object> resumeData = response.body();


                Log.d(TAG, "이력서 목록: " + resumeData.toString());
                Toast.makeText(ResumeManagementActivity.this, "이력서 조회 성공!", Toast.LENGTH_SHORT).show();

                containerLayout.addView(resumeCard);

                TextView tv_address = resumeCard.findViewById(R.id.tv_address);
                TextView tv_work_type = resumeCard.findViewById(R.id.tv_work_type);
                TextView tv_career = resumeCard.findViewById(R.id.tv_career);
                TextView createDate = resumeCard.findViewById(R.id.createDate);

                // ✅ 데이터가 없으면 기본 값 설정
                tv_address.setText(resumeData.containsKey("work_place_region") ? resumeData.get("work_place_region").toString() : "지역 정보 없음");
                tv_work_type.setText(resumeData.containsKey("industry_occupation") ? resumeData.get("industry_occupation").toString() : "직업 정보 없음");
                tv_career.setText(resumeData.containsKey("personal") ? resumeData.get("personal").toString() : "경력 정보 없음");

                if (resumeData.containsKey("last_modified_date")) {
                    String formattedDate = formatDate(resumeData.get("last_modified_date").toString());
                    createDate.setText(formattedDate + " 수정");
                } else {
                    createDate.setText("날짜 정보 없음");
                }

                btnMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showBottomSheetDialog();
                    }
                });

                Button btn_detail = findViewById(R.id.btn_detail);
                btn_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ResumeManagementActivity.this, ResumeDetailActivity.class);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "API 호출 실패: " + t.getMessage());
                Toast.makeText(ResumeManagementActivity.this, "네트워크 오류 발생!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showBottomSheetDialog() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_menu, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        LinearLayout btnDelete = bottomSheetView.findViewById(R.id.btn_delete);
        LinearLayout btnEmail = bottomSheetView.findViewById(R.id.btn_email);

        btnDelete.setOnClickListener(v -> {

            // 이력서 삭제 구현
            ResumeAPI appservice = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);
            Call<Void> call = appservice.deleteResume();

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.isSuccessful()) { // 200 OK 확인
                        Log.d("deleteResume", "삭제 요청 성공! 응답 코드: " + response.code());
                        Toast.makeText(ResumeManagementActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                        recreate();
                    } else {
                        Log.e("deleteResume", "삭제 실패 - 응답 코드: " + response.code());
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("deleteResumeFail", t.getMessage());
                    Log.d("deleteResumeFail", t.getMessage());
                    recreate();
                }
            });

            bottomSheetDialog.dismiss();
        });

        btnEmail.setOnClickListener(v -> {
            Toast.makeText(this, "이메일로 전달", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    public String formatDate(String inputDate) {
        // ✅ 원본 날짜 형식 (ISO 8601)
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        // ✅ 출력할 날짜 형식
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date date = inputFormat.parse(inputDate); // 문자열 -> Date 변환
            return outputFormat.format(date); // Date -> 원하는 형식의 문자열 변환
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate; // 변환 실패 시 원본 반환
        }
    }


}