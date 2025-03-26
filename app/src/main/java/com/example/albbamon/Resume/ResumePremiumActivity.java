package com.example.albbamon.Resume;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.albbamon.JobAdapter2;
import com.example.albbamon.R;
import com.example.albbamon.RecruitmentViewActivity;
import com.example.albbamon.aesbox.aesUtil;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.dto.response.GetRecruitmentApplyListResponseDto;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.network.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumePremiumActivity extends AppCompatActivity {
    private RecyclerView recyclerJobList;
    private JobAdapter2 jobAdapter2;
    private List<RecruitmentModel> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job2);

        recyclerJobList = findViewById(R.id.recyclerJobList);
        recyclerJobList.setLayoutManager(new LinearLayoutManager(this));

        jobList = new ArrayList<>();
        jobAdapter2 = new JobAdapter2(this, jobList);
        recyclerJobList.setAdapter(jobAdapter2);

        fetchRecruitmentPosts();

        // ✅ 지원하기 버튼 클릭 이벤트
        jobAdapter2.setOnItemClickListener(jobId -> {
            Intent intent = new Intent(ResumePremiumActivity.this, RecruitmentViewActivity.class);
            intent.putExtra("jobId", jobId);
            startActivity(intent);
        });
    }

    private void fetchRecruitmentPosts() {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);

        Call<ResponseBody> call = recruitmentAPI.getAllRecruitmentPosts(); // ✅ 새로운 API 호출

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    RecruitmentResponse responseModel = null;
                    SharedPreferences prefs = null;
                    try {
                        prefs = EncryptedSharedPreferences.create(
                                ResumePremiumActivity.this,
                                "secure_prefs",
                                new MasterKey.Builder(ResumePremiumActivity.this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        );
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String fixedKey = prefs.getString("aes_key", null);

                    try{
                        // 1. 응답 문자열 파싱
                        String responseBodyString = response.body().string();
                        Log.d("AES_DEBUG", "🔐 암호화된 평문 JSON: " + responseBodyString);

                        // 2. data 필드 추출 (Base64 문자열)
                        JsonObject root = JsonParser.parseString(responseBodyString).getAsJsonObject();
                        String base64EncryptedData = root.get("data").getAsString();

                        // 3. Base64 디코딩
                        byte[] encryptedBytes = android.util.Base64.decode(base64EncryptedData, android.util.Base64.DEFAULT);
                        Log.d("AES_DEBUG", "🔐 디코딩된 byte 길이: " + encryptedBytes.length);

                        // 4. AES 복호화
                        aesUtil aesUtil = new aesUtil(fixedKey);
                        String decryptedJson = aesUtil.decrypt(encryptedBytes);
                        Log.d("AES_DEBUG", "✅ 복호화된 평문 JSON: " + decryptedJson);

                        // 5. JSON → 객체 변환
                        responseModel = new Gson().fromJson(decryptedJson, RecruitmentResponse.class);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }




                    RecruitmentResponse recruitmentResponse = responseModel;
                    Log.d("API_RESPONSE", "Message: " + recruitmentResponse.getMessage());

                    jobList.clear();

                    // ✅ "recruitmentList" 내부의 데이터를 가져옴 (item = "Y" 필터링)
                    if (recruitmentResponse.getData() != null && recruitmentResponse.getData().getRecruitmentList() != null) {
                        for (RecruitmentModel job : recruitmentResponse.getData().getRecruitmentList()) {
                            if ("Y".equalsIgnoreCase(job.getItem())) {  // ✅ 대소문자 무시하고 비교
                                jobList.add(job);
                            }
                        }
                    } else {
                        Log.e("API_ERROR", "recruitmentList가 비어 있음.");
                        Toast.makeText(ResumePremiumActivity.this, "채용 공고 데이터 없음", Toast.LENGTH_SHORT).show();
                    }

                    // ✅ RecyclerView 업데이트
                    jobAdapter2.notifyDataSetChanged();
                } else {
                    Log.e("API_ERROR", "Response Failed. Body: " + response.errorBody());
                    Toast.makeText(ResumePremiumActivity.this, "채용 공고 데이터 로딩 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(ResumePremiumActivity.this, "채용 공고 API 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
