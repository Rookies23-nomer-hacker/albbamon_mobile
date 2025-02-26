package com.example.albbamon.Resume;

import android.content.Intent;
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
        String[] educationLevels = {"중학교", "고등학교", "대학교(2~3년제)", "대학교(4년제)", "대학원"};
        String[] statusOptions = {"졸업", "재학", "휴학", "중퇴"};

        SpinnerUtils.setupSpinner(this, spinnerEducation, educationLevels);
        SpinnerUtils.setupSpinner(this, spinnerStatus, statusOptions);

        // ✅ ResumeWriteActivity에서 전달받은 값이 있으면 Spinner 기본값 설정
        Intent intent = getIntent();
        if (intent.hasExtra("currentSchoolInfo")) {
            String currentSchoolInfo = intent.getStringExtra("currentSchoolInfo");
            if (currentSchoolInfo != null && currentSchoolInfo.contains(" ")) {
                String[] parts = currentSchoolInfo.split(" "); // "중학교 졸업"을 ["중학교", "졸업"]으로 분리
                String school = parts[0]; // 학력
                String status = parts[1]; // 상태

                // Spinner에서 해당 값의 인덱스를 찾아 기본값으로 설정
                setSpinnerSelection(spinnerEducation, educationLevels, school);
                setSpinnerSelection(spinnerStatus, statusOptions, status);
            }
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String school = spinnerEducation.getSelectedItem().toString();
            String status = spinnerStatus.getSelectedItem().toString();

            // ✅ 학력 정보를 ResumeDataManager에 저장
            ResumeDataManager.getInstance().setPersonalInfo(1L, school, status, "");

            // ✅ 저장 완료 토스트 메시지
            Toast.makeText(this, "학력 저장완료", Toast.LENGTH_SHORT).show();

            // ✅ ResumeWriteActivity로 이동 + 결과 값 전달
            Intent resultIntent = new Intent();
            resultIntent.putExtra("schoolContent", school + " " + status);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }
    // ✅ Spinner 기본값 설정 메서드 추가
    private void setSpinnerSelection(Spinner spinner, String[] options, String selectedValue) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selectedValue)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
