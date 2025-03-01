package com.example.albbamon.Resume;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.Resume.ResumeDataManager;

import com.example.albbamon.R;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResumePortfolioActivity extends AppCompatActivity {

    private Button btnFileUpload, btnSave;
    private RecyclerView fileRecyclerView;
    private PortfolioFileAdapter fileAdapter;
    private TextView tvEmptyPortfolio;
    private ArrayList<String> fileList;

    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_portfolio);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        LinearLayout layoutFileUpload = findViewById(R.id.layoutFileUpload);
        LinearLayout layoutUrl = findViewById(R.id.layoutUrl);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish());

        btnFileUpload = findViewById(R.id.btnFileUpload);
        btnSave = findViewById(R.id.btnSave);
        fileRecyclerView = findViewById(R.id.fileRecyclerView);
        tvEmptyPortfolio = findViewById(R.id.tvEmptyPortfolio);

        EditText etUrl = findViewById(R.id.etUrl);
        Button btnAddUrl = findViewById(R.id.btnAddUrl);

        // 기본 상태: 파일 업로드 탭 활성화
        layoutFileUpload.setVisibility(View.VISIBLE);
        layoutUrl.setVisibility(View.GONE);

        // ✅ 기존 포트폴리오 데이터 가져오기
        fileList = new ArrayList<>(ResumeDataManager.getInstance().getPortfolioList());
        fileAdapter = new PortfolioFileAdapter(fileList, this::updateEmptyState);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileRecyclerView.setAdapter(fileAdapter);

        updateEmptyState();

        // 탭 넘어가는거
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) { // 파일 업로드 탭 선택 시
                    layoutFileUpload.setVisibility(View.VISIBLE);
                    layoutUrl.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) { // URL 등록 탭 선택 시
                    layoutFileUpload.setVisibility(View.GONE);
                    layoutUrl.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 필요 없음
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 필요 없음
            }
        });

        // URL 추가 버튼 클릭 이벤트
        btnAddUrl.setOnClickListener(v -> {
            String url = etUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "올바른 URL을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!url.startsWith("http")) {
                Toast.makeText(this, "앞에 http를 붙여주세요", Toast.LENGTH_SHORT).show();
            } else {
                fileList.add(url); // ✅ URL을 리스트에 추가
                fileAdapter.notifyDataSetChanged();
                etUrl.setText(""); // 입력 필드 초기화
                updateEmptyState(); // UI 갱신
            }
        });

        btnFileUpload.setOnClickListener(v -> openFilePicker());

        btnSave.setOnClickListener(v -> {
            /*
            int fileCount = fileList.size();
            if (fileCount == 0) {
                Toast.makeText(this, "업로드할 포트폴리오가 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ 파일 목록을 데이터 매니저에 저장
            ResumeDataManager.getInstance().setPortfolio(fileList);

            // ✅ ResumeWriteActivity로 파일 개수 전달
            Intent resultIntent = new Intent();
            resultIntent.putExtra("portfolioContent", fileCount + "개");
            setResult(RESULT_OK, resultIntent);
            finish();
             */
            List<String> uploadedFiles = new ArrayList<>();

            // ✅ 파일 업로드 항목만 필터링 (URL 제외)
            for (String item : fileList) {
                if (!item.startsWith("[URL]")) {
                    uploadedFiles.add(item.replace("[FILE] ", ""));
                }
            }

            // ✅ 포트폴리오 파일이 2개 이상이면 저장하지 않음
            if (uploadedFiles.size() > 1) {
                Toast.makeText(this, "포트폴리오는 1개만 등록할 수 있습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ ResumeDataManager에 저장
//            ResumeDataManager.getInstance().setPortfolio(uploadedFiles);
            ResumeDataManager dataManager = ResumeDataManager.getInstance();
            dataManager.setPortfolio(uploadedFiles);

            if (uploadedFiles.isEmpty()) {
                // ✅ 포트폴리오가 없으면 null로 설정
                dataManager.setPortfolioName(null);
                dataManager.setPortfolioUrl(null);
                dataManager.setPortfolioData(null);
            } else {
                String fileName = uploadedFiles.get(0);
                String fileUrl = "http://upload/resume/portfolio/" + fileName;

                dataManager.setPortfolioName(fileName);
                dataManager.setPortfolioUrl(fileUrl);
            }

            // ✅ ResumeWriteActivity로 파일 개수 전달
            Intent resultIntent = new Intent();
            resultIntent.putExtra("portfolioContent", uploadedFiles.size() + "개");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "파일 선택"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedFileUri = data.getData();
                if (selectedFileUri != null) {
                    String fileName = getFileNameFromUri(selectedFileUri); // 파일명 가져오기
                    if (fileName != null && !fileName.isEmpty()) {
                        fileList.add(fileName);
                        fileAdapter.notifyDataSetChanged();
                        updateEmptyState();

                        // ✅ 파일을 서버로 업로드
                        uploadFileToServer(selectedFileUri, fileName);
                    }
                }
            }
        }
    }

    // 서버에 파일 업로드
    private void uploadFileToServer(Uri fileUri, String fileName) {
        try {
            String base64File = convertFileToBase64(fileUri);

            if (base64File == null) {
                Log.e("ERROR", "❌ 파일을 Base64로 변환하는 데 실패했습니다.");
                return;
            }

            ResumeDataManager dataManager = ResumeDataManager.getInstance();
            dataManager.setPortfolioData(base64File);
            dataManager.setPortfolioName(fileName);

            Log.d("DEBUG", "✅ 변환된 Base64 데이터 저장 완료: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "🚨 파일 변환 중 오류 발생: " + e.getMessage());
        }
    }



    private String convertFileToBase64(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            byte[] bytes;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            bytes = outputStream.toByteArray();
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    // ✅ URI에서 파일명 추출하는 함수 추가
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void updateEmptyState() {
        if (fileList.isEmpty()) {
            tvEmptyPortfolio.setVisibility(View.VISIBLE);
        } else {
            tvEmptyPortfolio.setVisibility(View.GONE);
        }
    }
}
