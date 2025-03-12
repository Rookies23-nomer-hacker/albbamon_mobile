package com.example.albbamon.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.api.UserAPI;
import com.example.albbamon.dto.request.UpdateApplyStatusRequestDto;
import com.example.albbamon.dto.request.UserRequestDto;
import com.example.albbamon.dto.response.GetUserInfoResponseDto;
import com.example.albbamon.dto.response.ResumeResponseDto;
import com.example.albbamon.model.UserData;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.network.SuccessResponse;
import com.example.albbamon.repository.UserRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitementResultActivity extends AppCompatActivity {

    private long recruitmentId, applyId, resumeId;
    private TextView nameText, phoneText, emailText;
    private TextView schoolContent, jobContent, optionContent, introContent, portfolioContent;
    private UserAPI userAPI;
    private String portfolioFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitement_result);

        userAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(UserAPI.class);

        Intent intent = getIntent();
        recruitmentId = intent.getLongExtra("recruitmentId", 0L);
        applyId = intent.getLongExtra("applyId", 0L);
        resumeId = intent.getLongExtra("resumeId", 0L);  // ✅ resumeId 추가

        Log.d("RecruitementResultActivity", "받은 recruitmentId: " + recruitmentId);
        Log.d("RecruitementResultActivity", "받은 applyId: " + applyId);
        Log.d("RecruitementResultActivity", "받은 resumeId: " + resumeId); // 로그 추가


        nameText = findViewById(R.id.Name);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        schoolContent = findViewById(R.id.schoolContent);
        jobContent = findViewById(R.id.jobContent);
        optionContent = findViewById(R.id.optionContent);
        introContent = findViewById(R.id.introContent);
        portfolioContent = findViewById(R.id.portfolioContent);


        findViewById(R.id.BackIcon).setOnClickListener(v -> finish());

        Button passButton = findViewById(R.id.passButton);
        Button failButton = findViewById(R.id.failButton);

        passButton.setOnClickListener(v -> updateApplyStatus("PASSED"));
        failButton.setOnClickListener(v -> updateApplyStatus("FAILED"));

        loadResumeData(resumeId);

        portfolioContent.setOnClickListener(v -> {
            if (!portfolioFileName.isEmpty()) {
                downloadPortfolioFile(portfolioFileName);
            } else {
                Toast.makeText(this, "포트폴리오 파일이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadResumeData(long resumeId) {
        ResumeAPI resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        Log.d("RecruitementResultActivity", "🔍 resumeId로 이력서 요청: " + resumeId);

        Call<Map<String, Object>> call = resumeAPI.getResumeById(resumeId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d("RecruitementResultActivity", "📡 서버 응답 수신! 응답 코드: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> resume = response.body();
                    Log.d("RecruitementResultActivity", "✅ 받은 이력서 데이터: " + resume);

                    // userId 가져오기
                    if (resume.containsKey("user_id")) {
                        long userId = ((Number) resume.get("user_id")).longValue();
                        Log.d("RecruitementResultActivity", "🔍 가져온 userId: " + userId);
                        loadUserInfo(userId);  // ✅ userId로 사용자 정보 조회
                    } else {
                        Log.e("RecruitementResultActivity", "❌ userId 정보 없음");
                    }

                    updateUI(resume);
                } else {
                    Log.e("RecruitementResultActivity", "❌ 서버 응답 실패: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "응답 본문 없음";
                        Log.e("RecruitementResultActivity", "오류 메시지: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("RecruitementResultActivity", "🚨 API 호출 실패: " + t.getMessage());
            }
        });
    }

    private void updateUI(Map<String, Object> resume) {
        schoolContent.setText((String) resume.get("school") + " " + resume.get("status"));
        introContent.setText((String) resume.get("introduction"));
        portfolioFileName = (String) resume.get("portfolioname");
        portfolioContent.setText(portfolioFileName);
    }

    private void updateApplyStatus(String status) {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);

        Log.d("RecruitementResultActivity", "📌 상태 변경 요청: status=" + status);

        UpdateApplyStatusRequestDto requestDto = new UpdateApplyStatusRequestDto(status);

        recruitmentAPI.updateApplyStatus(recruitmentId, applyId, requestDto).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // ✅ 응답을 JSON 객체로 파싱하여 처리
                        String responseString = response.body().string();
                        Log.d("RecruitementResultActivity", "✅ 상태 변경 성공: " + responseString);

                        Toast.makeText(RecruitementResultActivity.this, "상태가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("RecruitementResultActivity", "❌ 상태 변경 실패: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "없음";
                        Log.e("RecruitementResultActivity", "오류 메시지: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RecruitementResultActivity.this, "변경 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RecruitementResultActivity", "🚨 API 호출 오류: " + t.getMessage());
                Toast.makeText(RecruitementResultActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserInfo(long userId) {
        Log.d("RecruitementResultActivity", "🔍 userId로 사용자 정보 요청: " + userId);

        fetchUserInfoById(userId, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                Log.d("RecruitementResultActivity", "✅ 사용자 정보 조회 성공: " + new Gson().toJson(userInfo));
                updateUserInfo(userInfo);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("RecruitementResultActivity", "❌ 사용자 정보 조회 실패: " + errorMessage);
            }
        });
    }

    public void fetchUserInfoById(long userId, UserRepository.UserCallback callback) {
        Log.d("DEBUG", "🚀 fetchUserInfoById() 호출됨, userId: " + userId);

        Call<SuccessResponse<GetUserInfoResponseDto>> call = userAPI.getUserApplyerInfo(userId);
        call.enqueue(new Callback<SuccessResponse<GetUserInfoResponseDto>>() {
            @Override
            public void onResponse(Call<SuccessResponse<GetUserInfoResponseDto>> call, Response<SuccessResponse<GetUserInfoResponseDto>> response) {
                Log.d("DEBUG", "📌 API 응답 코드: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    GetUserInfoResponseDto responseDto = response.body().getData();

                    if (responseDto != null && responseDto.getUserInfo() != null) {
                        UserInfo userInfo = responseDto.getUserInfo(); // ✅ 여기서 변환
                        Log.d("DEBUG", "✅ fetchUserInfoById() 성공, userId: " + userInfo.getId());

                        if (userInfo.getId() != 0) {
                            callback.onSuccess(userInfo);
                        } else {
                            Log.e("ERROR", "❌ userId가 0입니다.");
                            callback.onFailure("userId가 0입니다.");
                        }
                    } else {
                        Log.d("DEBUG", "userInfo가 null입니다.");
                        callback.onFailure("userInfo가 null입니다.");
                    }
                } else {
                    try {
                        Log.e("API_ERROR", "서버 응답 실패 - 코드: " + response.code());
                        Log.e("API_ERROR", "응답 본문: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("API_ERROR", "응답 본문 읽기 실패", e);
                    }
                    callback.onFailure("응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<GetUserInfoResponseDto>> call, Throwable t) {
                Log.d("DEBUG", "API 호출 실패: " + t.getMessage());
                callback.onFailure("API 호출 실패: " + t.getMessage());
            }
        });
    }


    private void updateUserInfo(UserInfo userInfo) {
        String name = userInfo.getName();
        String phone = userInfo.getPhone();
        String email = userInfo.getEmail();

        nameText.setText(name);
        phoneText.setText(phone);
        emailText.setText(email);
    }

    private void downloadPortfolioFile(String fileName) {
        ResumeAPI resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        Log.d("RecruitementResultActivity", "📥 파일 다운로드 요청: " + fileName);
        Call<ResponseBody> call = resumeAPI.downloadResumeFile(fileName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isSaved = saveFileToStorage(response.body(), fileName);
                    if (isSaved) {
                        Toast.makeText(RecruitementResultActivity.this, "파일 다운로드 완료: " + fileName, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RecruitementResultActivity.this, "파일 저장 실패", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("RecruitementResultActivity", "❌ 다운로드 실패: " + response.code());
                    Toast.makeText(RecruitementResultActivity.this, "다운로드 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RecruitementResultActivity", "🚨 다운로드 요청 실패: " + t.getMessage());
                Toast.makeText(RecruitementResultActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean saveFileToStorage(ResponseBody body, String fileName) {
        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadDir, fileName);
            InputStream inputStream = null;
            FileOutputStream outputStream = null;

            try {
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                Log.e("RecruitementResultActivity", "❌ 파일 저장 실패", e);
                return false;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            Log.e("RecruitementResultActivity", "❌ 파일 저장 오류", e);
            return false;
        }
    }
}