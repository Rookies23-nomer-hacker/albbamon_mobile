package com.example.albbamon.Resume;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;

public class ResumeJobActivity extends AppCompatActivity {

    private RadioGroup radioGroupCareer;
    private RadioButton radioNewbie, radioExperienced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_job);

        // UI 요소 연결
        radioGroupCareer = findViewById(R.id.radioGroupCareer);
        radioNewbie = findViewById(R.id.radioNewbie);
        radioExperienced = findViewById(R.id.radioExperienced);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        // ✅ ResumeWriteActivity에서 전달받은 기존 값 설정 (기존 선택한 값 유지)
        Intent intent = getIntent();
        if (intent.hasExtra("currentJobInfo")) {
            String currentJobInfo = intent.getStringExtra("currentJobInfo");
            if ("신입".equals(currentJobInfo)) {
                radioNewbie.setChecked(true);
            } else if ("경력".equals(currentJobInfo)) {
                radioExperienced.setChecked(true);
            }
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            // ✅ 선택된 라디오 버튼 값 가져오기
            int selectedId = radioGroupCareer.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "경력 구분을 선택해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedCareer = (selectedId == R.id.radioNewbie) ? "신입" : "경력";

            // ✅ 선택한 경력 정보를 ResumeDataManager에 저장
            ResumeDataManager.getInstance().setWorkInfo("", "", "", selectedCareer);

            // ✅ 저장 완료 토스트 메시지
            Toast.makeText(this, "경력사항 저장완료", Toast.LENGTH_SHORT).show();

            // ✅ ResumeWriteActivity로 이동 + 결과 값 전달
            Intent resultIntent = new Intent();
            resultIntent.putExtra("jobContent", selectedCareer);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
