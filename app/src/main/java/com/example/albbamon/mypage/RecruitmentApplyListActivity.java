package com.example.albbamon.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.albbamon.R;
import com.example.albbamon.adapter.RecruitmentApplyAdapter;
import com.example.albbamon.aesbox.aesUtil;
import com.example.albbamon.dto.request.RecruitmentApplyListRequestDto;
import com.example.albbamon.dto.response.GetRecruitmentApplyListResponseDto;
import com.example.albbamon.dto.response.GetRecruitmentResponseDto;
import com.example.albbamon.model.RecruitmentApply;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.network.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitmentApplyListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecruitmentApplyAdapter adapter;
    private List<RecruitmentApply> applyList = new ArrayList<>();
    private Long recruitmentId; // 채용 공고 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_apply_list);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("지원서 관리하기");

        // Intent로부터 recruitmentId 받아오기
        Intent intent = getIntent();
        recruitmentId = intent.getLongExtra("recruitmentId", 0L); // 버튼 클릭 시 넘겨받은 recruitmentId
        String recruitmentTitle = intent.getStringExtra("recruitmentTitle"); // 공고 제목

        Log.d("RecruitmentApplyListActivity", "🔍 요청한 recruitmentId: " + recruitmentId);
        Log.d("RecruitmentApplyListActivity", "📌 요청한 recruitmentTitle: " + recruitmentTitle);

        // 공고 제목 UI에 표시
        TextView textRecruitmentTitle = findViewById(R.id.textRecruitmentTitle);
        if (recruitmentTitle != null && !recruitmentTitle.isEmpty()) {
            textRecruitmentTitle.setText(" \"" + recruitmentTitle + " \"" + "의 지원자 목록");
        } else {
            textRecruitmentTitle.setText("지원자 목록");
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecruitmentApplyAdapter(this, recruitmentId, applyList);
        recyclerView.setAdapter(adapter);

        loadApplyList();
    }

    private void loadApplyList() {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);

        try {
            RecruitmentApplyListRequestDto requestDto = new RecruitmentApplyListRequestDto(recruitmentId);

            // JSON 직렬화
            String json = new Gson().toJson(requestDto);
            Log.d("AES_DEBUG", "📤 요청 JSON: " + json);

            // 키 가져오기 (SharedPreferences에서)
            SharedPreferences prefs = EncryptedSharedPreferences.create(
                    this,
                    "secure_prefs",
                    new MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            String fixedKey = prefs.getString("aes_key", null);

            aesUtil aesUtil = new aesUtil(json);

            byte[] encryptedBytes = aesUtil.encrypt(json);
            Log.d("AES_DEBUG", "📤 암호화된 바이트 HEX: " + bytesToHex(encryptedBytes));


// Base64 인코딩
            String base64Encoded = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
            Log.d("AES_DEBUG", "📤 Base64 인코딩된 문자열: " + base64Encoded);


// JSON 문자열로 wrapping (따옴표 포함!)
            String jsonWrapped = "\"" + base64Encoded + "\"";
            Log.d("AES_DEBUG", "📤 최종 JSON 전송 문자열: " + jsonWrapped);


// RequestBody 생성 (text 아닌 application/json)
            RequestBody requestBody = RequestBody.create(
                    jsonWrapped,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Log.d("AES_DEBUG", "📤 RequestBody 객체: " + requestBody.toString());



            recruitmentAPI.getRecruitmentApplyList(base64Encoded).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("AES_DEBUG", "여기는 오면 안되는데");
                    Log.d("AES_DEBUG", String.valueOf(response.body()));
                    Log.d("AES_DEBUG", String.valueOf(response.isSuccessful()));

                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("AES_DEBUG", "여기는 오니ㅣㅣㅣㅣㅣㅣㅣㅣㅣ");
                        GetRecruitmentApplyListResponseDto responseDto = null;
                        try{
                            // 1. 응답 문자열 파싱
                            String responseBodyString = response.body();
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
                            responseDto = new Gson().fromJson(decryptedJson, GetRecruitmentApplyListResponseDto.class);


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }


                        Log.d("API_RESPONSE", "✅ 서버 응답 성공! " + responseDto.toString());
                        List<RecruitmentApply> recruitmentApplyList = responseDto.getApplyList();
                        applyList.clear();
                        if (recruitmentApplyList != null) {
                            applyList.addAll(recruitmentApplyList);
                            Log.d("API_RESPONSE", "📌 받아온 지원서 수: " + recruitmentApplyList.size());
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("API_RESPONSE", "🚨 서버 응답 오류: " + response.code());
                        Toast.makeText(RecruitmentApplyListActivity.this, "데이터 불러오기 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("API_ERROR", "❌ API 요청 실패: " + t.getMessage());
                    Toast.makeText(RecruitmentApplyListActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

}
