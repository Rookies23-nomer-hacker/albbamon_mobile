package com.example.albbamon.Experience;

import android.content.Intent;
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

        back_btn = findViewById(R.id.back_img_btn);
        btnUpload = findViewById(R.id.btn_upload);
        edit_title = findViewById(R.id.et_title);
        edit_content = findViewById(R.id.et_content);
        submit_btn = findViewById(R.id.btn_submit);
        char_count = findViewById(R.id.tv_character_count);


        //등록 버튼 클릭
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edit_title.getText().toString();
                Log.d("입력 받은 값", "제목 : " + title);
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
