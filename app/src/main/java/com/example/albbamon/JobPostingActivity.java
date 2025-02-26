package com.example.albbamon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.Calendar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.utils.SpinnerUtils;

import java.util.Calendar;


public class JobPostingActivity extends AppCompatActivity {

    private Spinner spinnerWageType;
    private TextView tvSelectedDate;
    private Button btnPickDate,btnUploadPhoto;
    private ImageView ivSelectedPhoto;
    private String selectedDate = "날짜 선택 안됨"; // 초기값 설정

    // 갤러리에서 사진 선택 결과 처리
    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        ivSelectedPhoto.setImageBitmap(bitmap); // 이미지 뷰에 표시
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "이미지 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ceo_job_posting);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        // XML에서 정의한 Spinner 가져오기
        spinnerWageType = findViewById(R.id.spinnerWageType);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        ivSelectedPhoto = findViewById(R.id.ivSelectedPhoto);

        SpinnerUtils.setupSpinner(this, spinnerWageType, new String[]{"월급", "일급", "시급", "건당"});

        btnPickDate.setOnClickListener(view -> showDatePicker());
        btnUploadPhoto.setOnClickListener(view -> openGallery()); // 사진 업로드 버튼 클릭 시 갤러리 실행


        findViewById(R.id.btnSave).setOnClickListener(v -> {
            Toast.makeText(JobPostingActivity.this,
                    "선택한 날짜: " + selectedDate,
                    Toast.LENGTH_SHORT).show();
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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }
}
