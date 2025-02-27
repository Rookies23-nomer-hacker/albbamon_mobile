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
import com.example.albbamon.dto.request.ResumeRequestDto;

import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.mypage.EditUserInfoActivity;
import com.example.albbamon.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.List;

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
    private UserAPI userAPI;
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
        ResumeDataManager dataManager = ResumeDataManager.getInstance();

        // ✅ 포트폴리오 데이터를 문자열로 변환 (쉼표로 구분된 파일명 리스트)
        List<String> portfolioFiles = dataManager.getPortfolioList();
        String portfolioData = String.join(",", portfolioFiles);

        // ✅ ResumeRequestDto 객체 생성
        ResumeRequestDto resumeData = new ResumeRequestDto(
                1L,  // ✅ 사용자 ID (임시값, 로그인된 사용자 정보에서 가져와야 함)
                dataManager.getSchool(),
                dataManager.getStatus(),
                dataManager.getPersonal(),
                dataManager.getWorkPlaceRegion(),
                dataManager.getWorkPlaceCity(),
                dataManager.getIndustryOccupation(),
                dataManager.getEmploymentType(),
                dataManager.getWorkingPeriod(),
                dataManager.getWorkingDay(),
                dataManager.getIntroduction(),
                portfolioData,
                dataManager.getPortfolioUrl(),
                dataManager.getPortfolioName(),
                "https://example.com/image.jpg",  // ✅ 이미지 URL (실제 앱에서는 업로드한 URL로 변경)
                "resume_image.jpg",  // ✅ 이미지 파일명
                "BASE64_ENCODED_STRING",  // ✅ Base64로 인코딩된 이미지 데이터
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // ✅ 서버로 데이터 전송
        resumeAPI.saveResume(1L, resumeData).enqueue(new Callback<ResumeResponseDto>() {
            @Override
            public void onResponse(Call<ResumeResponseDto> call, Response<ResumeResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 실패", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResumeResponseDto> call, Throwable t) {
                Toast.makeText(ResumeWriteActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "이력서 저장 실패", t);
            }
        });
    }


    private void saveResumeToServer() {
        ResumeDataManager dataManager = ResumeDataManager.getInstance();
        ResumeRequestDto resumeData = dataManager.toResumeRequestDto();

        // ✅ userId를 ResumeDataManager에서 가져옴
        long userId = dataManager.getUserId();

        resumeAPI.saveResume(userId, resumeData).enqueue(new Callback<ResumeResponseDto>() {
            @Override
            public void onResponse(Call<ResumeResponseDto> call, Response<ResumeResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 실패", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResumeResponseDto> call, Throwable t) {
                Toast.makeText(ResumeWriteActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "이력서 저장 실패", t);
            }
        });
    }

    private void saveResumeToServer() {
        ResumeDataManager dataManager = ResumeDataManager.getInstance();
        ResumeRequestDto resumeData = dataManager.toResumeRequestDto();

        // ✅ 로그인된 사용자 정보에서 userId 가져오기
        long userId = userRepository.getUserInfo().getUserId();

        resumeAPI.saveResume(userId, resumeData).enqueue(new Callback<ResumeResponseDto>() {
            @Override
            public void onResponse(Call<ResumeResponseDto> call, Response<ResumeResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResumeWriteActivity.this, "이력서 저장 실패", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResumeResponseDto> call, Throwable t) {
                Toast.makeText(ResumeWriteActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "이력서 저장 실패", t);
            }
        });
    }




}
