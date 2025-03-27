package com.example.albbamon.mypage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.albbamon.MainActivity;
import com.example.albbamon.R;
import com.example.albbamon.adapter.MyRecruitmentAdapter;
import com.example.albbamon.aesbox.aesUtil;
import com.example.albbamon.model.MyRecruitment;
import com.example.albbamon.dto.response.GetRecruitmentResponseDto;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.network.SuccessResponse;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.network.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecruitmentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyRecruitmentAdapter adapter;
    private RecruitmentAPI recruitmentAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recruitment_list);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("공고관리");

        findViewById(R.id.back).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Retrofit을 이용한 API 객체 생성
        recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);

        // ✅ 채용 공고 목록 불러오기
        loadMyRecruitmentList();

        findViewById(R.id.back).setOnClickListener(v -> finish()); // 현재 액티비티 종료

    }

    private void loadMyRecruitmentList() {
        recruitmentAPI.getMyRecruitments().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                    GetRecruitmentResponseDto responseDto = null;
                    SharedPreferences prefs = null;
                    try {
                        prefs = EncryptedSharedPreferences.create(
                                MyRecruitmentListActivity.this,
                                "secure_prefs",
                                new MasterKey.Builder(MyRecruitmentListActivity.this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        );
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String fixedKey = prefs.getString("aes_key", null);
//                    Log.d("AES_DEBUG", "복호화 키 길이: " + fixedKey.length());
//                    Log.d("AES_DEBUG", "복호화 키 값: " + fixedKey);


                    try{
                        // 1. 응답 문자열 파싱
                        String responseBodyString = response.body().string();
//                        Log.d("AES_DEBUG", "🔐 암호화된 평문 JSON: " + responseBodyString);

                        // 2. data 필드 추출 (Base64 문자열)
                        JsonObject root = JsonParser.parseString(responseBodyString).getAsJsonObject();
                        String base64EncryptedData = root.get("data").getAsString();

                        // 3. Base64 디코딩
                        byte[] encryptedBytes = android.util.Base64.decode(base64EncryptedData, android.util.Base64.DEFAULT);
//                        Log.d("AES_DEBUG", "🔐 디코딩된 byte 길이: " + encryptedBytes.length);

                        // 4. AES 복호화
                        aesUtil aesUtil = new aesUtil(fixedKey);
                        String decryptedJson = aesUtil.decrypt(encryptedBytes);
//                        Log.d("AES_DEBUG", "✅ 복호화된 평문 JSON: " + decryptedJson);

                        // 5. JSON → 객체 변환
                        responseDto = new Gson().fromJson(decryptedJson, GetRecruitmentResponseDto.class);


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                    GetRecruitmentResponseDto recruitmentData = responseDto; // ✅ `data` 필드 추출
                    if (recruitmentData != null && recruitmentData.getRecruitmentList() != null) {
                        List<MyRecruitment> recruitmentList = recruitmentData.getRecruitmentList(); // ✅ 실제 리스트 추출

                        // ✅ recruitmentId가 null인지 확인
                        Log.d("MyRecruitmentListActivity", "📌 받은 recruitmentList: " + recruitmentList.size() + "개");
                        for (MyRecruitment recruitment : recruitmentList) {
                            Log.d("MyRecruitmentListActivity", "📌 recruitmentId: " + recruitment.getRecruitmentId() +
                                    ", title: " + recruitment.getTitle() + ", company: " + recruitment.getCompany());
                        }

                        adapter = new MyRecruitmentAdapter(recruitmentList, MyRecruitmentListActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(MyRecruitmentListActivity.this, "채용 공고가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyRecruitmentListActivity.this, "데이터 불러오기 실패", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "서버 응답 오류: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MyRecruitmentListActivity.this, "API 요청 실패", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "네트워크 오류: " + t.getMessage());
            }
        });
    }
}
