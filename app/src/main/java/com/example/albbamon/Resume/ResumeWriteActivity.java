package com.example.albbamon.Resume;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.albbamon.R;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.api.UserAPI;
import com.example.albbamon.model.ResumeRequestDto;
import com.example.albbamon.model.ResumeUserModel;
import com.example.albbamon.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ResumeWriteActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCHOOL = 1001;
    private static final int REQUEST_CODE_JOB = 1002;
    private ImageView backIcon;
    private Button btnSave, btnEditProfile;
    private TextView nameText, phoneText, emailText;
    private TextView schoolContent, jobContent;
    private UserAPI userAPI;
    private ResumeAPI resumeAPI;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.resume_write);

        // UI 요소 연결
        backIcon = findViewById(R.id.BackIcon);
        btnSave = findViewById(R.id.btnSave);
        btnEditProfile = findViewById(R.id.userEdit);
        scrollView = findViewById(R.id.scrollView);

        nameText = findViewById(R.id.Name);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);

        schoolContent = findViewById(R.id.schoolContent);
        jobContent = findViewById(R.id.jobContent);

        // 뒤로 가기 버튼 클릭 이벤트
        backIcon.setOnClickListener(v -> finish()); // 현재 액티비티 종료

        // Retrofit 클라이언트 생성
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        userAPI = retrofit.create(UserAPI.class);
        resumeAPI = retrofit.create(ResumeAPI.class);

        // 사용자 정보 가져오기
        fetchUserInfo();

        // 회원정보 수정 버튼 클릭 이벤트
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(ResumeWriteActivity.this, "회원정보 수정 화면으로 이동", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, UserEditActivity.class));  // 회원정보 수정 화면 이동 시 사용
        });

        // 이력서 저장 버튼 클릭 이벤트
        btnSave.setOnClickListener(v -> saveResume());

        // ScrollView를 맨 위로 이동
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));
    }

    /**
     * 사용자 정보 가져오는 함수입니다.
     */
    private void fetchUserInfo() {
        userAPI.getUserInfo().enqueue(new Callback<ResumeUserModel>() {
            @Override
            public void onResponse(Call<ResumeUserModel> call, Response<ResumeUserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResumeUserModel user = response.body();

                    // ✅ 전체 JSON 응답을 로그로 출력
                    Log.d("API_SUCCESS", "전체 응답: " + response.body().toString());

                    // ✅ API 응답 데이터 로그 출력
                    Log.d("API_SUCCESS", "사용자 정보: "
                            + (user.getName() != null ? user.getName() : "null") + ", "
                            + (user.getEmail() != null ? user.getEmail() : "null") + ", "
                            + (user.getPhone() != null ? user.getPhone() : "null"));

                    // UI 업데이트
                    nameText.setText(user.getName() != null ? user.getName() : "이름 없음");
                    phoneText.setText(user.getPhone() != null ? user.getPhone() : "전화번호 없음");
                    emailText.setText(user.getEmail() != null ? user.getEmail() : "이메일 없음");

                } else {
                    Log.e("API_ERROR", "사용자 정보 가져오기 실패: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResumeUserModel> call, Throwable t) {
                Log.e("API_ERROR", "API 호출 실패", t);
            }
        });
    }

    /**
     * 학력 정보 이동 함수
     */
    public void goToSchoolPage(View view) {
        Intent intent = new Intent(this, ResumeSchoolActivity.class);
        // ✅ 현재 schoolContent 값을 Intent에 담아 전달
        String currentSchoolInfo = schoolContent.getText().toString();
        intent.putExtra("currentSchoolInfo", currentSchoolInfo);
        startActivityForResult(intent, REQUEST_CODE_SCHOOL);
//        Toast.makeText(ResumeWriteActivity.this, "학력사항 개발중", Toast.LENGTH_SHORT).show();
    }

    /**
     * 경력정보 이동 함수
     */
    public void goToJobPage(View view) {
        Intent intent = new Intent(this, ResumeJobActivity.class);
        String currentJobInfo = jobContent.getText().toString();
        intent.putExtra("currentJobInfo", currentJobInfo);
        startActivityForResult(intent, REQUEST_CODE_JOB);
//        Toast.makeText(ResumeWriteActivity.this, "경력사항 개발중", Toast.LENGTH_SHORT).show();
    }

    /**
     * 희망근무조건 이동 함수
     */
    public void goToOptionPage(View view) {
        Intent intent = new Intent(this, ResumeOptionActivity.class);
        startActivity(intent);
//        Toast.makeText(ResumeWriteActivity.this, "희망근무조건 개발중", Toast.LENGTH_SHORT).show();

    }

    /**
     * 자기소개 이동 함수
     */
    public void goToIntroPage(View view) {
        Intent intent = new Intent(this, ResumeIntroActivity.class);
        startActivity(intent);
//        Toast.makeText(ResumeWriteActivity.this, "자기소개 개발중", Toast.LENGTH_SHORT).show();

    }

    /**
     * 포트폴리오 이동 함수
     */
    public void goToPortfolioPage(View view) {
        Intent intent = new Intent(this, ResumePortfolioActivity.class);
        startActivity(intent);
//        Toast.makeText(ResumeWriteActivity.this, "포트폴리오 개발중", Toast.LENGTH_SHORT).show();

    }

    // ✅ 학력/경력 페이지에서 결과를 받아 UI 업데이트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SCHOOL) {
                String schoolInfo = data.getStringExtra("schoolContent");
                schoolContent.setText(schoolInfo);
            } else if (requestCode == REQUEST_CODE_JOB) {
                String jobInfo = data.getStringExtra("jobContent");
                jobContent.setText(jobInfo);
            }
        }
    }

    private void saveResume() {
        // ✅ ResumeDataManager에서 데이터 가져오기
        ResumeRequestDto resumeData = ResumeDataManager.getInstance().toResumeRequestDto();

        resumeAPI.createResume(resumeData).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 실패", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ResumeWriteActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "이력서 저장 실패", t);
            }
        });
    }


}
