package com.example.albbamon.Experience;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.albbamon.R;
import com.example.albbamon.SignIn;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.network.RetrofitClient;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienceCreate extends AppCompatActivity {
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_PERMISSION = 100;

    private ImageButton back_btn, btnUpload;
    private ImageView upload_image;
    private EditText edit_title, edit_content;
    private TextView submit_btn, char_count;
    private Uri selectedFileUri = null; // 선택한 파일 URI 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_create);

        // 세션에서 사용자 ID 가져오기
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ExperienceCreate.this, SignIn.class));
            finish();
            return;
        }

        // UI 요소 초기화
        back_btn = findViewById(R.id.back_img_btn);
        btnUpload = findViewById(R.id.btn_upload);
        upload_image = findViewById(R.id.upload_image);
        edit_title = findViewById(R.id.et_title);
        edit_content = findViewById(R.id.et_content);
        submit_btn = findViewById(R.id.btn_submit);
        char_count = findViewById(R.id.tv_character_count);

        CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);

        // 파일 업로드 버튼 클릭 시 파일 선택 (권한 체크 후)
        btnUpload.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                openGallery(); // 버튼 클릭 시만 파일 선택 창 열기
            }
        });

        // 파일 미리보기 클릭 시 삭제 다이얼로그 표시
        upload_image.setOnClickListener(view -> {
            if (selectedFileUri != null) {
                showRemoveFileDialog();
            }
        });

        // 등록 버튼 클릭 (API 호출)
        submit_btn.setOnClickListener(view -> createPostAction(userId, apiService, selectedFileUri));

        // 글자 수 업데이트
        edit_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                char_count.setText(charSequence.length() + "/5000");
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // 뒤로 가기 버튼
        back_btn.setOnClickListener(view -> finish());
    }

    // ✅ 갤러리 열기
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    // ✅ 파일 선택 후 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                upload_image.setImageURI(selectedFileUri);
                upload_image.setVisibility(View.VISIBLE);
            }
        }
    }

    // ✅ 파일 삭제 여부 확인 다이얼로그
    private void showRemoveFileDialog() {
        new AlertDialog.Builder(this)
                .setTitle("파일 삭제")
                .setMessage("선택한 파일을 제거하시겠습니까?")
                .setPositiveButton("예", (dialog, which) -> {
                    selectedFileUri = null;
                    upload_image.setVisibility(View.GONE);
                })
                .setNegativeButton("아니오", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // ✅ 게시글 작성 API 호출 (파일 업로드 포함)
    private void createPostAction(long userId, CommunityAPI apiService, Uri fileUri) {
        String title = edit_title.getText().toString();
        String content = edit_content.getText().toString();

        // RequestBody 변환
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content);

        MultipartBody.Part filePart = null;

        if (fileUri != null) {
            File file = new File(getRealPathFromURI(fileUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        }

        Call<Void> call = apiService.createPost(filePart, userIdBody, titleBody, contentBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ExperienceCreate.this, "게시글이 작성되었습니다!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ExperienceCreate.this, ExperienceList.class));
                    finish();
                } else {
                    try {
                        String errorMsg = response.errorBody().string();
                        Log.e("API_ERROR", "Error Response: " + errorMsg);
                        Toast.makeText(ExperienceCreate.this, "작성 실패!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    // ✅ Uri → 파일 경로 변환
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();

        return path;
    }
}
