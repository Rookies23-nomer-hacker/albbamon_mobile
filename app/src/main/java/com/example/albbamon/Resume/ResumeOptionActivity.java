package com.example.albbamon.Resume;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;
import com.example.albbamon.utils.SpinnerUtils;

public class ResumeOptionActivity extends AppCompatActivity {

    private Spinner spinnerCity, spinnerRegion, spinnerOccupation, spinnerPeriod, spinnerJobDates;
    private RadioGroup radioGroupJobType;
    private RadioButton radioPartTime, radioFullTime, radioContract;
    private ResumeDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_option);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish());

        // XML에서 정의한 Spinner 및 RadioGroup 가져오기
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        spinnerOccupation = findViewById(R.id.spinnerOccupation);
        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        spinnerJobDates = findViewById(R.id.spinnerJobDates);
        radioGroupJobType = findViewById(R.id.radioGroupJobType);
        radioPartTime = findViewById(R.id.radioPartTime);
        radioFullTime = findViewById(R.id.radioFullTime);
        radioContract = findViewById(R.id.radioContract);

        // ✅ ResumeDataManager 인스턴스 가져오기
        dataManager = ResumeDataManager.getInstance();

        // ✅ Spinner 데이터 설정
        String[] cityOptions = {"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"};
        String[] regionOptions = {"전체", "시/군/구"};
        String[] occupationOptions = {"선택", "서비스", "사무직", "IT기술", "디자인"};
        String[] periodOptions = {"무관", "하루", "1주일 이하", "1개월 ~ 3개월", "3개월 ~ 6개월", "6개월 ~ 1년", "1년 이상"};
        String[] jobDatesOptions = {"전체", "주1회", "주2회", "주3회", "주4회", "주5회", "주6회", "주7회"};

        SpinnerUtils.setupSpinner(this, spinnerCity, cityOptions);
        SpinnerUtils.setupSpinner(this, spinnerRegion, regionOptions);
        SpinnerUtils.setupSpinner(this, spinnerOccupation, occupationOptions);
        SpinnerUtils.setupSpinner(this, spinnerPeriod, periodOptions);
        SpinnerUtils.setupSpinner(this, spinnerJobDates, jobDatesOptions);

        // ✅ 기존 선택값 UI에 반영 (onResume에서도 실행)
        updateUI();

        // ✅ 라디오 버튼 선택 시 즉시 `ResumeDataManager`에 저장
        radioGroupJobType.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedJobType = "";
            if (checkedId == R.id.radioPartTime) {
                selectedJobType = "알바";
            } else if (checkedId == R.id.radioFullTime) {
                selectedJobType = "정규직";
            } else if (checkedId == R.id.radioContract) {
                selectedJobType = "계약직";
            }
            dataManager.setEmploymentType(selectedJobType);
        });

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String selectedCity = spinnerCity.getSelectedItem().toString();
            String selectedRegion = spinnerRegion.getSelectedItem().toString();
            String selectedOccupation = spinnerOccupation.getSelectedItem().toString();
            String selectedPeriod = spinnerPeriod.getSelectedItem().toString();
            String selectedJobDates = spinnerJobDates.getSelectedItem().toString();

            // ✅ 선택한 값 ResumeDataManager에 저장
            dataManager.setWorkInfo(selectedCity, selectedRegion, selectedOccupation, dataManager.getEmploymentType());
            dataManager.setWorkingConditions(selectedPeriod, selectedJobDates);

            Toast.makeText(this, "희망근무조건 저장완료", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("optionContent", selectedCity + " " + selectedRegion + " " + selectedOccupation + " 등");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();  // ✅ 액티비티가 다시 열릴 때 UI 업데이트
    }

    private void updateUI() {
        setSpinnerSelection(spinnerPeriod, new String[]{"무관", "하루", "1주일 이하", "1개월 ~ 3개월", "3개월 ~ 6개월", "6개월 ~ 1년", "1년 이상"}, dataManager.getWorkingPeriod());
        setSpinnerSelection(spinnerJobDates, new String[]{"전체", "주1회", "주2회", "주3회", "주4회", "주5회", "주6회", "주7회"}, dataManager.getWorkingDay());

        // ✅ 기존 선택한 근무형태 유지
        String selectedJobType = dataManager.getEmploymentType();
        if ("알바".equals(selectedJobType)) {
            radioPartTime.setChecked(true);
        } else if ("정규직".equals(selectedJobType)) {
            radioFullTime.setChecked(true);
        } else if ("계약직".equals(selectedJobType)) {
            radioContract.setChecked(true);
        }
    }

    private void setSpinnerSelection(Spinner spinner, String[] options, String selectedValue) {
        if (selectedValue != null) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(selectedValue)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }
}
