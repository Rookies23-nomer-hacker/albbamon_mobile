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
import com.example.albbamon.dto.request.ResumeRequestDto;
import com.example.albbamon.mypage.ResumeManagementActivity;
import com.example.albbamon.network.RetrofitClient;

import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.mypage.EditUserInfoActivity;
import com.example.albbamon.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeWriteActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCHOOL = 1001;
    private static final int REQUEST_CODE_JOB = 1002;
    private static final int REQUEST_CODE_OPTION = 1003;
    private static final int REQUEST_CODE_INTRO = 1004;
    private static final int REQUEST_CODE_PORTFOLIO = 1005;

    private ImageView backIcon;
    private Button btnSave;
    private TextView nameText, phoneText, emailText;
    private TextView schoolContent, jobContent, optionContent, introContent, portfolioContent;
    private ResumeAPI resumeAPI;
    private ScrollView scrollView;

    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.resume_write);

        // UI 요소 연결
        backIcon = findViewById(R.id.BackIcon);
        btnSave = findViewById(R.id.btnSave);
        scrollView = findViewById(R.id.scrollView);

        nameText = findViewById(R.id.Name);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);

        schoolContent = findViewById(R.id.schoolContent);
        jobContent = findViewById(R.id.jobContent);
        optionContent = findViewById(R.id.optionContent);
        introContent = findViewById(R.id.introContent);
        portfolioContent = findViewById(R.id.portfolioContent);

        // 뒤로 가기 버튼 클릭 이벤트
        backIcon.setOnClickListener(v -> finish()); // 현재 액티비티 종료

        // UserRepository 초기화
        userRepository = new UserRepository(this);

        // fetchUserInfo() 호출하여 사용자 정보 가져오기
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                nameText.setText(userInfo.getName() != null ? userInfo.getName() : "이름 없음");
                phoneText.setText(userInfo.getPhone() != null ? userInfo.getPhone() : "전화번호 없음");
                emailText.setText(userInfo.getEmail() != null ? userInfo.getEmail() : "이메일 없음");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ResumeWriteActivity", "사용자 정보 가져오기 실패: " + errorMessage);
            }
        });

        resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        // ScrollView를 맨 위로 이동
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));

        // 이력서 저장 버튼 클릭 이벤트
        btnSave.setOnClickListener(v -> {
            if (!isValidInput()) {  // ✅ 필수 입력값이 비어 있으면 실행 중단
                return;
            }
            saveResumeToServer();
        });


        // 회원정보 수정 버튼 클릭
        findViewById(R.id.userEdit).setOnClickListener( v -> {
            Intent intent = new Intent(this, EditUserInfoActivity.class);
            startActivity(intent);
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
        String currentOptionInfo = optionContent.getText().toString();
        intent.putExtra("currentOptionInfo", currentOptionInfo);
        startActivityForResult(intent, REQUEST_CODE_OPTION);
//        Toast.makeText(ResumeWriteActivity.this, "희망근무조건 개발중", Toast.LENGTH_SHORT).show();

    }

    /**
     * 자기소개 이동 함수
     */
    public void goToIntroPage(View view) {
        Intent intent = new Intent(this, ResumeIntroActivity.class);
        startActivityForResult(intent, REQUEST_CODE_INTRO);
//        Toast.makeText(ResumeWriteActivity.this, "자기소개 개발중", Toast.LENGTH_SHORT).show();

    }

    /**
     * 포트폴리오 이동 함수
     */
    public void goToPortfolioPage(View view) {
        Intent intent = new Intent(this, ResumePortfolioActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PORTFOLIO);
//        Toast.makeText(ResumeWriteActivity.this, "포트폴리오 개발중", Toast.LENGTH_SHORT).show();

    }

    // ✅ 각 액티비티에서 결과를 받아 UI 업데이트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SCHOOL) {
                String schoolInfo = data.getStringExtra("schoolContent");
                Log.d("DEBUG", "📌 학력 사항 업데이트: " + schoolInfo);
                schoolContent.setText(schoolInfo);
            } else if (requestCode == REQUEST_CODE_JOB) {
                String jobInfo = data.getStringExtra("jobContent");
                Log.d("DEBUG", "📌 경력 사항 업데이트: " + jobInfo);
                jobContent.setText(jobInfo);
            } else if (requestCode == REQUEST_CODE_OPTION) {
                String optionInfo = data.getStringExtra("optionContent");
                Log.d("DEBUG", "📌 희망 근무조건 업데이트: " + optionInfo);
                optionContent.setText(optionInfo);
            } else if (requestCode == REQUEST_CODE_INTRO) { // ✅ 자기소개 추가
                String introInfo = data.getStringExtra("introContent");
                Log.d("DEBUG", "📌 자기소개 업데이트: " + introInfo);
                introContent.setText(introInfo);
            } else if (requestCode == REQUEST_CODE_PORTFOLIO) { // ✅ 포트폴리오 개수 추가
                String portfolioInfo = data.getStringExtra("portfolioContent");
                Log.d("DEBUG", "📌 포트폴리오 업데이트: " + portfolioInfo);
                portfolioContent.setText(portfolioInfo);
            }
        }
    }

    private boolean isValidInput() {
        ResumeDataManager dataManager = ResumeDataManager.getInstance();

        if (dataManager.getSchool() == null || dataManager.getSchool().trim().isEmpty()) {
            Toast.makeText(this, "학력사항을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataManager.getPersonal() == null || dataManager.getPersonal().trim().isEmpty()) {
            Toast.makeText(this, "경력사항을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataManager.getEmploymentType() == null || dataManager.getEmploymentType().trim().isEmpty()) {
            Toast.makeText(this, "희망근무조건을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataManager.getIntroduction() == null || dataManager.getIntroduction().trim().isEmpty()) {
            Toast.makeText(this, "자기소개를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void saveResumeToServer() {
        Log.d("DEBUG", "🚀 saveResumeToServer() 호출됨");

        ResumeDataManager dataManager = ResumeDataManager.getInstance();

        // ✅ API 요청 전 데이터 확인 로그 추가
        Log.d("DEBUG-W", "📌 API 요청 전 포트폴리오 데이터 확인");
        Log.d("DEBUG-W", "portfolioName: " + dataManager.getPortfolioName());
        Log.d("DEBUG-W", "portfolioUrl: " + dataManager.getPortfolioUrl());

        ResumeRequestDto resumeData = dataManager.toResumeRequestDto();

        long userId = userRepository.getUserId();
        Log.d("DEBUG", "📌 가져온 userId: " + userId);

        if (resumeAPI == null) { // ✅ resumeAPI가 null인지 체크
            Log.e("ERROR", "❌ resumeAPI가 null입니다. Retrofit 초기화 확인 필요.");
            return;
        }

        sendResumeRequest(userId, resumeData);
//        sendResumeRequest(userId, resumeData);
    }

    private void sendResumeRequest(long userId, ResumeRequestDto resumeData) {
        Log.d("DEBUG", "🚀 sendResumeRequest() 호출됨, userId: " + userId);

        if (resumeAPI == null) {
            Log.e("ERROR", "❌ sendResumeRequest() 실행 중 resumeAPI가 null입니다.");
            return;
        }

        resumeAPI.saveResume(userId, resumeData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("DEBUG", "📌 API 응답 코드: " + response.code());

                if (response.isSuccessful()) {
                    try {
                        // ✅ 응답 본문을 String으로 변환
                        String responseBody = response.body() != null ? response.body().string() : "";
                        Log.d("DEBUG", "📌 서버 응답 메시지: " + responseBody);

                        Toast.makeText(ResumeWriteActivity.this, responseBody, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResumeWriteActivity.this, ResumeManagementActivity.class);
                        startActivity(intent);
                        finish(); // 현재 액티비티 종료

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ERROR", "❌ 이력서 저장 실패, 응답 코드: " + response.code());

                    try {
                        String errorResponse = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("ERROR", "📌 서버 오류 메시지: " + errorResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR", "🚨 네트워크 오류 발생! 메시지: " + t.getMessage(), t);
                Toast.makeText(ResumeWriteActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
