package com.example.albbamon.Resume;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;
import com.example.albbamon.utils.SpinnerUtils;

public class ResumeSchoolActivity extends AppCompatActivity {

    private Spinner spinnerEducation, spinnerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_school);

        // XML에서 정의한 Spinner 가져오기
        spinnerEducation = findViewById(R.id.spinnerEducation);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        // ✅ 공통 스피너 설정
        SpinnerUtils.setupSpinner(this, spinnerEducation, new String[]{"중학교", "고등학교", "대학교(2~3년제)", "대학교(4년제)", "대학원"});
        SpinnerUtils.setupSpinner(this, spinnerStatus, new String[]{"졸업", "재학", "휴학", "중퇴"});

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            Toast.makeText(ResumeSchoolActivity.this, "학력 저장완료", Toast.LENGTH_SHORT).show();
        });
    }
}
