package com.example.albbamon.mypage;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.albbamon.R;
import com.example.albbamon.model.JobItemModel;
import java.util.ArrayList;
import java.util.List;

public class MyJobPostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobItemAdapter jobItemAdapter;
    private List<JobItemModel> jobItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_job_post);

        recyclerView = findViewById(R.id.recycler_job_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobItemList = new ArrayList<>();
        jobItemAdapter = new JobItemAdapter(jobItemList, this);
        recyclerView.setAdapter(jobItemAdapter);

        // 더미 데이터 추가
        addDummyData();
    }

    private void addDummyData() {
        jobItemList.add(new JobItemModel("2024.07.03", "온라인지원", "맨파워그룹", "[1일단기] 시험감독 아르바이트", "07.07"));
        jobItemList.add(new JobItemModel("2024.06.20", "이메일지원", "삼성전자", "사무보조 아르바이트", "06.30"));
        jobItemList.add(new JobItemModel("2024.06.18", "방문지원", "롯데마트", "매장 관리 직원", "06.25"));

        jobItemAdapter.notifyDataSetChanged();
    }
}
