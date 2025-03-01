package com.example.albbamon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.file.FileUtils;
import com.example.albbamon.file.MultipartUtils;
import com.example.albbamon.model.JobPostingModel;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.network.SuccessResponse;
import com.example.albbamon.utils.SpinnerUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class JobPostingActivity extends AppCompatActivity {

    private Spinner spinnerWageType;
    private TextView tvSelectedDate;
    private Button btnPickDate,btnUploadPhoto, btnSave;
    private ImageView ivSelectedPhoto;
    private String selectedDate = "날짜 선택 안됨"; // 초기값 설정
//    private EditText title;
//    private EditText contents;
//    private TextView dueDate;
//    private EditText wage;
    private Bitmap fileBitmap;
    private MultipartBody.Part file;


    // 갤러리에서 사진 선택 결과 처리
    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        ivSelectedPhoto.setImageBitmap(bitmap); // 이미지 뷰에 표시
                        fileBitmap = bitmap;
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
        //월급, 일급, 시급, 건당 선택
        spinnerWageType = findViewById(R.id.spinnerWageType);
        // 날짜를 선택하세요 -> 날짜 선택 시 해당 날짜 적힘
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        // 날짜 선택 버튼
        btnPickDate = findViewById(R.id.btnPickDate);
        // 사진 선택 버튼
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        // 공고 사진
        ivSelectedPhoto = findViewById(R.id.ivSelectedPhoto);
        // 등록 버튼
        btnSave = findViewById(R.id.btnSave);

        SpinnerUtils.setupSpinner(this, spinnerWageType, new String[]{"월급", "일급", "시급", "건당"});

        btnPickDate.setOnClickListener(view -> showDatePicker());
        btnUploadPhoto.setOnClickListener(view -> openGallery()); // 사진 업로드 버튼 클릭 시 갤러리 실행
        btnSave.setOnClickListener(view -> submitJobPost());

         Spinner spinnerWageType;
         TextView tvSelectedDate;
         Button btnPickDate,btnUploadPhoto;
         ImageView ivSelectedPhoto;
         String selectedDate = "날짜 선택 안됨"; // 초기값 설정

        // 등록 버튼 클릭 이벤트
        btnSave.setOnClickListener(view -> submitJobPost());






//        findViewById(R.id.btnSave).setOnClickListener(v -> {
//            Toast.makeText(JobPostingActivity.this,
//                    "선택한 날짜: " + selectedDate,
//                    Toast.LENGTH_SHORT).show();
//        });

    }

    private void submitJobPost(){
        String title = ((EditText) findViewById(R.id.etTitle)).getText().toString();
        String contents = ((EditText) findViewById(R.id.etContents)).getText().toString();

        TextView dueDateView = findViewById(R.id.tvSelectedDate);
        String dueDateText = dueDateView.getText().toString();


        // ✅ LocalDate로 변환 후, LocalDateTime으로 변환 (시간 00:00:00 추가)
        LocalDate dueDate = LocalDate.parse(dueDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime dueDateTime = dueDate.atStartOfDay();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDueDate = LocalDate.parse(dueDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .atStartOfDay()
                .format(formatter);






        EditText wageView  = findViewById(R.id.etWageAmount);
        Integer wage = Integer.parseInt(wageView.getText().toString());

        file = changeImage(fileBitmap);

        RecruitmentAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);
        JobPostingModel jobPostingModel = new JobPostingModel(title, contents, formattedDueDate, wage);

        Log.d("JobPost", "Title: " + title);
        Log.d("JobPost", "Contents: " + contents);
        Log.d("JobPost", "Due Date: " + formattedDueDate);
        Log.d("JobPost", "Wage: " + wage);


        Call<SuccessResponse<Void>> call = apiService.createRecruitment(file, jobPostingModel);

        call.enqueue(new Callback<SuccessResponse<Void>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Void>> call, Response<SuccessResponse<Void>> response) {
                Log.d("API_RESPONSE", "JobPosting 코드: " + response.code());

            }

            @Override
            public void onFailure(Call<SuccessResponse<Void>> call, Throwable t) {

            }
        });



    }












    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // ✅ 항상 두 자리 숫자로 변환하여 yyyy-MM-dd 형식 유지
                    selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    tvSelectedDate.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private MultipartBody.Part changeImage(Bitmap bitmap){
        // ✅ 1. Bitmap을 File로 변환
        File imageFile = FileUtils.bitmapToFile(this, bitmap);

        // ✅ 2. File을 MultipartBody.Part로 변환
        MultipartBody.Part imagePart = MultipartUtils.fileToMultipart(imageFile, "image");

        // ✅ 3. 파일 경로 로그 출력 (File에서 가져옴)
        Log.d("JobPost", "File Path: " + imageFile.getAbsolutePath());

// ✅ 4. MultipartBody.Part에 대한 정보 출력
        Log.d("JobPost", "File Name (Multipart): " + imagePart.headers());

        return imagePart;


    }


}
