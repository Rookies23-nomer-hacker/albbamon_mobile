package com.example.albbamon.Experience;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.albbamon.R;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.api.ResponseWrapper;
import com.example.albbamon.dto.request.CreatePostRequestDto;
import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienceUpdate extends AppCompatActivity {
    ImageButton back_img_btn, btn_upload;
    TextView btn_submit;
    EditText et_title, et_content;
    long postId, userId;
    String old_title, old_content, edit_title, edit_content, file_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_update);
        postId = getIntent().getLongExtra("postId", -1);
        Log.d("DetailActivity", "Post ID: " + postId);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        btn_submit = findViewById(R.id.btn_submit);
        back_img_btn = findViewById(R.id.back_img_btn);
        btn_upload = findViewById(R.id.btn_upload);

        // SharedPreferences에서 사용자 ID 가져오기
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        userId = prefs.getLong("userId", -1); // 기본값 -1 (저장된 값이 없을 경우)
        Log.d("Session", "User ID (로그인 사용자): " + userId);

        // API 호출 (게시글 정보 가져오기)
        fetchPostData();

        // 뒤로 가기 버튼
        back_img_btn.setOnClickListener(view -> finish());

        // 등록 버튼
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_title = et_title.getText().toString().strip();
                edit_content = et_content.getText().toString().strip() ;
                if(edit_title.equals(old_title) && edit_content.equals(old_content)){
                   //변경된 내용이 없을 때
                    Toast.makeText(ExperienceUpdate.this, "수정된 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                }else{

                    CreatePostRequestDto requestDto = new CreatePostRequestDto(userId, edit_title, edit_content,file_name);
                    CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);
                    Call<Void> call = apiService.updatePost(postId, requestDto);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {

                                Toast.makeText(ExperienceUpdate.this, "게시글이 수정되었습니다!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ExperienceUpdate.this, ExperienceView.class);
                                intent.putExtra("postId", postId);
                                startActivity(intent);
                            } else {
                                try {
                                    String errorMsg = response.errorBody().string(); // 서버 응답 에러 메시지
                                    Log.e("API_ERROR", "Error Response: " + errorMsg);
                                    Toast.makeText(ExperienceUpdate.this, "수정 실패!", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ExperienceUpdate.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("API_ERROR", t.getMessage());
                        }
                    });
                }

            }
        });

    }

    // 게시글 데이터 불러오기 (비동기 API)
    private void fetchPostData() {
        CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);

        Call<ResponseWrapper<CommunityModel>> call = apiService.getPostById(postId);
        call.enqueue(new Callback<ResponseWrapper<CommunityModel>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<CommunityModel>> call, Response<ResponseWrapper<CommunityModel>> response) {
                Log.d("API_RESPONSE", "Raw Response: " + response.raw());

                if (response.isSuccessful() && response.body() != null) {
                    CommunityModel bbs = response.body().getData();
                    if (bbs != null) {
                        et_title.setText(bbs.getTitle());
                        et_content.setText(bbs.getContents());
                        old_title = et_title.getText().toString().strip();
                        old_content = et_content.getText().toString().strip();
                        file_name = bbs.getFile_name().toString();
                    } else {
                        Log.e("API_ERROR", "data 필드가 null 입니다.");
                    }
                } else {
                    Log.e("API_ERROR", "서버 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<CommunityModel>> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}