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
    private ResumeDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_job);

        radioGroupCareer = findViewById(R.id.radioGroupCareer);
        radioNewbie = findViewById(R.id.radioNewbie);
        radioExperienced = findViewById(R.id.radioExperienced);
        dataManager = ResumeDataManager.getInstance();

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish());

        // ✅ 기존 값 UI에 반영
        updateUI();

        radioGroupCareer.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioNewbie) {
                dataManager.setPersonal("신입");
            } else if (checkedId == R.id.radioExperienced) {
                dataManager.setPersonal("경력");
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String selectedCareer = dataManager.getPersonal(); // ✅ personal 값 가져오기
            Toast.makeText(this, "경력사항 저장완료", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("jobContent", selectedCareer);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    // ✅ 액티비티 재진입 시 기존 값 반영
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        String jobType = dataManager.getEmploymentType();
        if ("신입".equals(jobType)) {
            radioNewbie.setChecked(true);
        } else if ("경력".equals(jobType)) {
            radioExperienced.setChecked(true);
        }
    }
}
