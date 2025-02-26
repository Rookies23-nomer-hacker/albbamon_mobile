package com.example.albbamon.Resume;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.google.android.material.tabs.TabLayout;

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

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish());

        btnFileUpload = findViewById(R.id.btnFileUpload);
        btnSave = findViewById(R.id.btnSave);
        fileRecyclerView = findViewById(R.id.fileRecyclerView);
        tvEmptyPortfolio = findViewById(R.id.tvEmptyPortfolio);

        // ✅ 기존 포트폴리오 데이터 가져오기
        fileList = new ArrayList<>(ResumeDataManager.getInstance().getPortfolioList());
        fileAdapter = new PortfolioFileAdapter(fileList, this::updateEmptyState);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileRecyclerView.setAdapter(fileAdapter);

        updateEmptyState();

        btnFileUpload.setOnClickListener(v -> openFilePicker());

        btnSave.setOnClickListener(v -> {
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
                    String fileName = selectedFileUri.getLastPathSegment();
                    fileList.add(fileName);
                    fileAdapter.notifyDataSetChanged();
                    updateEmptyState();
                }
            }
        }
    }

    private void updateEmptyState() {
        if (fileList.isEmpty()) {
            tvEmptyPortfolio.setVisibility(View.VISIBLE);
        } else {
            tvEmptyPortfolio.setVisibility(View.GONE);
        }
    }
}
