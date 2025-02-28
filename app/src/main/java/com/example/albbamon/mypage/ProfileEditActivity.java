package com.example.albbamon.mypage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.albbamon.R;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.dto.request.ProfileImageRequestDto;
import com.example.albbamon.dto.response.ProfileImageResponseDto;
import com.example.albbamon.network.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Uri imageUri; // 선택한 이미지 URI

    private ResumeAPI resumeAPI; // Retrofit API 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_edit);

        // 툴바 타이틀 설정
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("프로필 사진");

        // 시스템 바 인셋 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI 요소 초기화
        profileImageView = findViewById(R.id.profile_image);
        Button btnSelectImage = findViewById(R.id.btn_select_image);

        // Retrofit 초기화 (서버 base URL은 실제 주소로 변경)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://yourserver.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        resumeAPI = retrofit.create(ResumeAPI.class);

        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileEditActivity.this, UserMypageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 버튼 클릭 시 갤러리에서 사진 선택
        btnSelectImage.setOnClickListener(v -> openImageChooser());
    }

    // 갤러리에서 이미지 선택하는 메서드
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지 선택"), PICK_IMAGE_REQUEST);
    }

    // 선택한 이미지 처리 후 서버에 전송
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // 선택한 이미지를 Bitmap으로 변환 및 ImageView에 설정
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);

                // Bitmap을 Base64 문자열로 변환
                String base64Image = convertBitmapToBase64(bitmap);

                // 서버에 프로필 이미지 업데이트 요청
                updateProfileImageOnServer(base64Image);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "이미지 처리 중 오류 발생", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Bitmap -> Base64 문자열 변환 메서드
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 이미지 압축 설정 (필요시 품질 조정)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    private void updateProfileImageOnServer(String base64Image) {
        ProfileImageRequestDto requestDto = new ProfileImageRequestDto(base64Image);

        // ✅ 세션 포함 Retrofit 인스턴스 사용
        ResumeAPI resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);

        Call<ProfileImageResponseDto> call = resumeAPI.updateProfileImage(requestDto);
        call.enqueue(new Callback<ProfileImageResponseDto>() {
            @Override
            public void onResponse(Call<ProfileImageResponseDto> call, Response<ProfileImageResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileImageResponseDto responseBody = response.body();
                    if (responseBody.getError() != null) {
                        Log.e("ProfileUpdate", "오류: " + responseBody.getError());
                        Toast.makeText(ProfileEditActivity.this, "업데이트 실패: " + responseBody.getError(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("ProfileUpdate", "성공: " + responseBody.getMessage());
                        Log.d("ProfileUpdate", "이미지 이름: " + responseBody.getResume_img_name());
                        Log.d("ProfileUpdate", "이미지 URL: " + responseBody.getResume_img_url());
                        Toast.makeText(ProfileEditActivity.this, "프로필 이미지 업데이트 성공", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("ProfileUpdate", "실패: HTTP " + response.code() + " - " + response.message() + "\n에러 메시지: " + errorBody);
                    } catch (IOException e) {
                        Log.e("ProfileUpdate", "에러 본문 읽기 실패", e);
                    }
                    Toast.makeText(ProfileEditActivity.this, "업데이트 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileImageResponseDto> call, Throwable t) {
                Log.e("ProfileUpdate", "서버 요청 실패", t);
                Toast.makeText(ProfileEditActivity.this, "서버 통신 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
