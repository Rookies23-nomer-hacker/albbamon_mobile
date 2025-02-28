package com.example.albbamon.mypage;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.albbamon.R;
import com.example.albbamon.model.ResumeModel;
import java.util.ArrayList;
import java.util.List;

public class ManagementApplyer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ResumeAdapter resumeAdapter;
    private List<ResumeModel> resumeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_management_applyer);

        recyclerView = findViewById(R.id.recycler_resume_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        resumeList = new ArrayList<>();
        resumeAdapter = new ResumeAdapter(resumeList);
        recyclerView.setAdapter(resumeAdapter);

        // API 호출 후 데이터 업데이트
        fetchResumeData();
    }

    private void fetchResumeData() {
        resumeList.add(new ResumeModel("2025-02-18 수정", "경력 5년 2개월", "서울 성북구, 서울 종로구", "키즈카페, DVD-멀티방"));
        resumeList.add(new ResumeModel("2024-12-10 수정", "경력 3년 6개월", "부산 해운대구", "IT 개발, 디자인"));

        resumeAdapter.notifyDataSetChanged();
    }
}
