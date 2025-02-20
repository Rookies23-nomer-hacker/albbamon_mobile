package com.example.albbamon;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import com.example.albbamon.utils.SpinnerUtils;

public class ResumeOptionActivity extends AppCompatActivity {

    private TextView tvSelectedDate;
    private Button btnPickDate;
    private Spinner spinnerCity, spinnerRegion, spinnerOccupation, spinnerPeriod;
    private RadioGroup radioGroupJobType;
    private String selectedDate = "날짜 선택 안됨"; // 초기값 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_option);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnPickDate = findViewById(R.id.btnPickDate);
        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        spinnerOccupation = findViewById(R.id.spinnerOccupation);
        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        radioGroupJobType = findViewById(R.id.radioGroupJobType);

        // ✅ 공통 메서드 사용해서 스피너 설정
        SpinnerUtils.setupSpinner(this, spinnerCity, new String[]{"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"});
        SpinnerUtils.setupSpinner(this, spinnerRegion, new String[]{"전체", "시/군/구"});
        SpinnerUtils.setupSpinner(this, spinnerOccupation, new String[]{"선택", "서비스", "사무직", "IT기술", "디자인"});
        SpinnerUtils.setupSpinner(this, spinnerPeriod, new String[]{"무관", "하루", "1주일 이하", "1개월 ~ 3개월", "3개월 ~ 6개월", "6개월 ~ 1년", "1년 이상"});

        btnPickDate.setOnClickListener(view -> showDatePicker());

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String selectedCity = spinnerCity.getSelectedItem().toString();
            String selectedRegion = spinnerRegion.getSelectedItem().toString();
            String selectedOccupation = spinnerOccupation.getSelectedItem().toString();
            String selectedPeriod = spinnerPeriod.getSelectedItem().toString();
            String selectedJobType = getSelectedJobType();

            Toast.makeText(this,
                    "근무지: " + selectedCity + " " + selectedRegion +
                            "\n업직종: " + selectedOccupation +
                            "\n근무형태: " + selectedJobType +
                            "\n근무기간: " + selectedPeriod +
                            "\n근무일시: " + selectedDate,
                    Toast.LENGTH_LONG).show();
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    tvSelectedDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private String getSelectedJobType() {
        int selectedId = radioGroupJobType.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return "선택 안됨";
        } else {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
    }
}
