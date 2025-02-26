package com.example.albbamon.Resume;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;

public class ResumeIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_intro);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            Toast.makeText(ResumeIntroActivity.this, "자기소개 저장완료", Toast.LENGTH_SHORT).show();
        });
    }
}
