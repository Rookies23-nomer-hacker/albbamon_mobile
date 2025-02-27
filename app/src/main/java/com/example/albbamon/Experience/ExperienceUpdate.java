package com.example.albbamon.Experience;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienceUpdate extends AppCompatActivity {
    ImageButton back_img_btn, btn_upload;
    TextView btn_submit;
    EditText et_title, et_content;
    long postId, userId;
    String old_title, old_content, edit_title, edit_content;
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
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_title = et_title.getText().toString().strip();
                edit_content = et_content.getText().toString().strip() ;
                if(edit_title.equals(old_title)){
                    Log.e("test", "수정된 값"+edit_title);
                }
//                CreatePostRequestDto requestDto = new CreatePostRequestDto(userId, edit_title, edit_content,"");
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