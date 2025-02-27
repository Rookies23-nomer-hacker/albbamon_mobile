package com.example.albbamon.Experience;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.albbamon.R;
import com.example.albbamon.SignIn;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.dto.request.CreatePostRequestDto;
import com.example.albbamon.model.LoginUserModel;
import com.example.albbamon.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ExperienceCreate extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_FILE = 3;

    private ImageButton back_btn;
    private ImageButton btnUpload;
    private EditText edit_title;
    private EditText edit_content;
    private TextView submit_btn;
    TextView char_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_create);

        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1); // 기본값 -1 (저장된 값이 없을 경우)

        if (userId != -1) {
            // userId가 정상적으로 저장되어 있다면 사용 가능
            Log.d("Session", "User ID: " + userId);
        } else {
            // userId 값이 저장되지 않았거나 오류 발생
            Log.d("Session", "User ID가 저장되지 않음");
            Toast.makeText(this, "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ExperienceCreate.this, SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 이전 화면 제거
            startActivity(intent);
            finish();
        }

        back_btn = findViewById(R.id.back_img_btn);
        btnUpload = findViewById(R.id.btn_upload);
        edit_title = findViewById(R.id.et_title);
        edit_content = findViewById(R.id.et_content);
        submit_btn = findViewById(R.id.btn_submit);
        char_count = findViewById(R.id.tv_character_count);

        CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);



        //등록 버튼 클릭
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPostAction(userId,apiService);
                }
            });

        //contet 입력 감지(글자 수 업데이트)
        edit_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int charCount = charSequence.length();
                char_count.setText(charCount + "/5000");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        back_btn.setOnClickListener(view -> finish());
        btnUpload.setOnClickListener(this::showPopupMenu);
    }


    private void createPostAction(long userId, CommunityAPI apiService){
        String title = edit_title.getText().toString();
        String content = edit_content.getText().toString();
        CreatePostRequestDto requestDto = new CreatePostRequestDto(userId, title, content,"");
        Call<Void> call = apiService.createPost(requestDto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(ExperienceCreate.this, "게시글이 작성되었습니다!", Toast.LENGTH_SHORT).show();
                    finish(); // 액티비티 종료
                } else {
                    try {
                        String errorMsg = response.errorBody().string(); // 서버 응답 에러 메시지
                        Log.e("API_ERROR", "Error Response: " + errorMsg);
                        Toast.makeText(ExperienceCreate.this, "작성 실패!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ExperienceCreate.this, "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
    // 팝업 메뉴를 표시하는 메서드
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.upload_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_gallery) {
                openGallery();
                return true;
            } else if (item.getItemId() == R.id.menu_camera) {
                openCamera();
                return true;
            } else if (item.getItemId() == R.id.menu_file) {
                openFileChooser();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    // 갤러리 열기
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    // 카메라 실행
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    // 파일 선택기 열기
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // 모든 파일 선택 가능
        startActivityForResult(intent, REQUEST_FILE);
    }

    // 선택한 파일 처리 (갤러리, 카메라, 파일)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();
            if (selectedUri != null) {
                switch (requestCode) {
                    case REQUEST_GALLERY:
                        Toast.makeText(this, "갤러리에서 선택됨: " + selectedUri, Toast.LENGTH_SHORT).show();
                        break;
                    case REQUEST_FILE:
                        Toast.makeText(this, "파일 선택됨: " + selectedUri, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Toast.makeText(this, "사진이 촬영되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
