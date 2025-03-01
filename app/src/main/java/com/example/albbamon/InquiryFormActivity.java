package com.example.albbamon;  // 🚨 패키지 확인!

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ✅ **클래스를 선언해야 함!**
public class InquiryFormActivity extends AppCompatActivity {

    private Spinner spinnerMainCategory, spinnerSubCategory;
    private EditText etInquiryContent, etInquiryEmail, etInquiryPhone;
    private CheckBox cbAgreePrivacy;
    private Button btnSubmitInquiry;
    private TextView tvAttachedFile;
    private Uri attachedFileUri;
    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inquiries);  // ✅ 올바른 XML 확인!

        Log.d("InquiryFormActivity", "onCreate 실행됨!"); // 로그 추가

        // 🔹 뒤로 가기 버튼 설정
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack == null) {
            Log.e("InquiryFormActivity", "btnBack이 NULL! XML에서 btn_back ID를 확인하세요.");
            Toast.makeText(this, "btn_back이 XML에 없습니다!", Toast.LENGTH_LONG).show();
        } else {
            btnBack.setOnClickListener(v -> finish());
        }

        // 🔹 레이아웃에서 뷰 찾아오기
        spinnerMainCategory = findViewById(R.id.spinner_main_category);
        spinnerSubCategory = findViewById(R.id.spinner_sub_category);
        etInquiryContent = findViewById(R.id.et_inquiry_content);
        etInquiryEmail = findViewById(R.id.et_inquiry_email);
        etInquiryPhone = findViewById(R.id.et_inquiry_phone);
        cbAgreePrivacy = findViewById(R.id.cb_agree_privacy);
        btnSubmitInquiry = findViewById(R.id.btn_submit_inquiry);
        tvAttachedFile = findViewById(R.id.tv_attached_file);

        // 🔹 파일 선택 API 설정
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                attachedFileUri = uri;
                String fileName = getFileName(uri);
                tvAttachedFile.setText("첨부된 파일: " + fileName);
            }
        });

        // 🔹 파일 첨부 버튼 설정
        FrameLayout attachmentBox = findViewById(R.id.attachment_box);
        if (attachmentBox != null) {
            attachmentBox.setOnClickListener(v -> filePickerLauncher.launch("*/*"));
        } else {
            Log.e("InquiryFormActivity", "attachmentBox가 NULL입니다. XML ID를 확인하세요.");
        }

        // 🔹 개인정보 동의 체크 확인 후 전송 버튼 클릭 가능
        btnSubmitInquiry.setOnClickListener(v -> {
            if (!cbAgreePrivacy.isChecked()) {
                Toast.makeText(this, "개인정보 동의를 해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "문의가 전송되었습니다.", Toast.LENGTH_SHORT).show();
        });
    }

    private String getFileName(Uri uri) {
        String lastSegment = uri.getLastPathSegment();
        return lastSegment != null ? lastSegment : "알 수 없음";
    }
}
