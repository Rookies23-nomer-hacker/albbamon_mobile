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
import com.example.albbamon.network.RetrofitClient;

import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.mypage.EditUserInfoActivity;
import com.example.albbamon.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private Button btnSave, btnEditProfile;
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
        btnEditProfile = findViewById(R.id.userEdit);
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
//        resumeAPI = RetrofitClient.getRetrofitInstance().create(ResumeAPI.class); // ✅ 초기화 추가


        // 회원정보 수정 버튼 클릭 이벤트
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(ResumeWriteActivity.this, "회원정보 수정 화면으로 이동", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, UserEditActivity.class));  // 회원정보 수정 화면 이동 시 사용
        });

        // 이력서 저장 버튼 클릭 이벤트
        btnSave.setOnClickListener(v -> saveResumeToServer());

        // ScrollView를 맨 위로 이동
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_UP));

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
                schoolContent.setText(schoolInfo);
            } else if (requestCode == REQUEST_CODE_JOB) {
                String jobInfo = data.getStringExtra("jobContent");
                jobContent.setText(jobInfo);
            } else if (requestCode == REQUEST_CODE_OPTION) {
                String optionInfo = data.getStringExtra("optionContent");
                optionContent.setText(optionInfo);
            } else if (requestCode == REQUEST_CODE_INTRO) { // ✅ 자기소개 추가
                String introInfo = data.getStringExtra("introContent");
                introContent.setText(introInfo);
            } else if (requestCode == REQUEST_CODE_PORTFOLIO) { // ✅ 포트폴리오 개수 추가
                    String portfolioInfo = data.getStringExtra("portfolioContent");
                    portfolioContent.setText(portfolioInfo);
            }
        }
    }

    private void saveResumeToServer() {
        Log.d("DEBUG", "🚀 saveResumeToServer() 호출됨");

        ResumeDataManager dataManager = ResumeDataManager.getInstance();
        ResumeRequestDto resumeData = dataManager.toResumeRequestDto();

        long userId = userRepository.getUserId();
        Log.d("DEBUG", "📌 가져온 userId: " + userId);

        if (resumeAPI == null) { // ✅ resumeAPI가 null인지 체크
            Log.e("ERROR", "❌ resumeAPI가 null입니다. Retrofit 초기화 확인 필요.");
            return;
        }

        if (userId == 0L) {
            Log.d("DEBUG", "⚠️ userId가 0이므로 fetchUserInfo() 호출");
            userRepository.fetchUserInfo(new UserRepository.UserCallback() {
                @Override
                public void onSuccess(UserInfo userInfo) {
                    long updatedUserId = userInfo.getId();
                    Log.d("DEBUG", "✅ fetchUserInfo() 성공, userId: " + updatedUserId);
                    sendResumeRequest(updatedUserId, resumeData);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("ERROR", "❌ fetchUserInfo() 실패: " + errorMessage);
                    Toast.makeText(ResumeWriteActivity.this, "로그인 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("DEBUG", "✅ userId가 존재하므로 바로 sendResumeRequest() 실행");
            sendResumeRequest(userId, resumeData);
        }
    }

    private void sendResumeRequest(long userId, ResumeRequestDto resumeData) {
        Log.d("DEBUG", "🚀 sendResumeRequest() 호출됨, userId: " + userId);

        if (resumeAPI == null) {
            Log.e("ERROR", "❌ sendResumeRequest() 실행 중 resumeAPI가 null입니다.");
            return;
        }

        // ✅ LocalDateTime을 ISO 8601 형식으로 변환하도록 설정
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>)
                        (localDateTime, type, jsonSerializationContext) ->
                                jsonSerializationContext.serialize(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .create();

        String jsonData = gson.toJson(resumeData);
        Log.d("DEBUG", "📌 요청 데이터 (LocalDateTime 변환 적용됨): " + jsonData);

        resumeAPI.saveResume(userId, resumeData).enqueue(new Callback<ResumeResponseDto>() {
            @Override
            public void onResponse(Call<ResumeResponseDto> call, Response<ResumeResponseDto> response) {
                Log.d("DEBUG", "📌 API 응답 코드: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("DEBUG", "✅ 이력서 저장 성공");
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ERROR", "❌ 이력서 저장 실패, 응답 코드: " + response.code());
                    try {
                        String errorResponse = response.errorBody().string();
                        Log.e("ERROR", "📌 서버 응답 메시지: " + errorResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResumeResponseDto> call, Throwable t) {
                Log.e("ERROR", "🚨 네트워크 오류: " + t.getMessage());
                Toast.makeText(ResumeWriteActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }







}
