package com.example.albbamon;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
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
import com.example.albbamon.repository.UserRepository;
import com.example.albbamon.utils.SpinnerUtils;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
                        file = convertBitmapToMultipart(this, bitmap, "upload_image");
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

        // ✅ 사용자 정보 확인 후 접근 차단
        UserRepository userRepository = new UserRepository(this);
        userRepository.isUserCeo(isCeo -> {
            if (!isCeo) {
                Toast.makeText(this, "일반 사용자는 접근할 수 없습니다.", Toast.LENGTH_LONG).show();
                finish(); // 🚫 액티비티 종료
                return;
            }
            // 🔹 CEO 사용자만 UI 초기화 진행
            initUI();
        });
    }

    private void initUI() {
        findViewById(R.id.BackIcon).setOnClickListener(v -> finish()); // 현재 액티비티 종료

        spinnerWageType = findViewById(R.id.spinnerWageType);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        ivSelectedPhoto = findViewById(R.id.ivSelectedPhoto);
        btnSave = findViewById(R.id.btnSave);

        SpinnerUtils.setupSpinner(this, spinnerWageType, new String[]{"월급", "일급", "시급", "건당"});

        btnPickDate.setOnClickListener(view -> showDatePicker());
        btnUploadPhoto.setOnClickListener(view -> openGallery());
        btnSave.setOnClickListener(view -> submitJobPost());
    }

    private void submitJobPost() {
        String title = ((EditText) findViewById(R.id.etTitle)).getText().toString();
        String contents = ((EditText) findViewById(R.id.etContents)).getText().toString();
        TextView dueDateView = findViewById(R.id.tvSelectedDate);
        String dueDateText = dueDateView.getText().toString();

        // ✅ LocalDate 변환 후, LocalDateTime으로 변환 (시간 00:00:00 추가)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDueDate = LocalDate.parse(dueDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .atStartOfDay()
                .format(formatter);

        EditText wageView = findViewById(R.id.etWageAmount);
        Integer wage = Integer.parseInt(wageView.getText().toString());

        // ✅ JobPostingModel 객체 생성
        JobPostingModel jobPostingModel = new JobPostingModel(title, contents, formattedDueDate, wage);

        // ✅ JobPostingModel을 JSON 문자열로 변환
        RequestBody jobPostingBody = RequestBody.create(
                new Gson().toJson(jobPostingModel),
                okhttp3.MediaType.parse("application/json")
        );

        // ✅ 이미지 파일 처리
        MultipartBody.Part filePart = null;
        if (fileBitmap != null) {
            filePart = changeImage(fileBitmap);
        }

        // ✅ API 호출
        RecruitmentAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);
        Call<SuccessResponse<Void>> call = apiService.createRecruitment(file, jobPostingBody);

        call.enqueue(new Callback<SuccessResponse<Void>>() {
            @Override
            public void onResponse(Call<SuccessResponse<Void>> call, Response<SuccessResponse<Void>> response) {
                Log.d("API_RESPONSE", "JobPosting 코드: " + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(JobPostingActivity.this, "공고 등록 성공!", Toast.LENGTH_SHORT).show();
                    finish(); // 현재 액티비티 종료
                } else {
                    Log.e("API_ERROR", "공고 등록 실패: " + response.errorBody());
                    Toast.makeText(JobPostingActivity.this, "공고 등록 실패!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<Void>> call, Throwable t) {
                Log.e("API_FAILURE", "네트워크 오류: " + t.getMessage());
                Toast.makeText(JobPostingActivity.this, "네트워크 오류 발생!", Toast.LENGTH_SHORT).show();
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

    public static MultipartBody.Part convertBitmapToMultipart(Context context, Bitmap bitmap, String fileName) {
        // ✅ 1. Bitmap을 임시 파일로 저장
        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(filesDir, fileName + ".jpg");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
        } catch (IOException e) {
            Log.e("ImageUtils", "파일 변환 중 오류 발생", e);
            return null;
        }

        // ✅ 2. RequestBody 생성
        RequestBody requestFile = RequestBody.create(imageFile, MediaType.parse("image/jpeg"));

        // ✅ 3. MultipartBody.Part 생성
        return MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);
    }

}
