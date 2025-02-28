package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JobListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private List<JobItem> jobList;
    private Button btnJobList;  // 채용공고 목록 버튼 추가
    private ImageView btnSearch;
    private Spinner spinnerFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        // 뷰 초기화
        recyclerView = findViewById(R.id.recyclerJobList);
        btnSearch = findViewById(R.id.btnSearch);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        btnJobList = findViewById(R.id.btnJobList); // 채용공고 목록 버튼 초기화

        // 검색 버튼 클릭 시 검색 화면으로 이동
        btnSearch.setOnClickListener(v -> {
            Log.d("JobListActivity", "검색 버튼 클릭됨");
            Intent intent = new Intent(JobListActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // 채용공고 목록 버튼 클릭 이벤트 추가
        btnJobList.setOnClickListener(v -> {
            Log.d("JobListActivity", "채용공고 목록 버튼 클릭됨");
            Intent intent = new Intent(JobListActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // 채용공고 목록 더미 데이터 추가
        jobList = new ArrayList<>();
        jobList.add(new JobItem("고객상담사 채용", "국민건강보험 대구센터", "대구 서구", "2,530,000원", "14분전"));
        jobList.add(new JobItem("설비IOP 모집", "남녀O모집 보너스 지급", "대구 전지역", "4,000,000원", "30분전"));
        jobList.add(new JobItem("주방 직원 모집", "동백명주", "대구", "2,100,000원", "1시간전"));

    }
}
