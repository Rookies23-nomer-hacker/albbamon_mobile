package com.example.albbamon.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.example.albbamon.adapter.RecruitmentApplyAdapter;
import com.example.albbamon.dto.response.GetRecruitmentApplyListResponseDto;
import com.example.albbamon.model.RecruitmentApply;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.model.SuccessResponse;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruitmentApplyListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecruitmentApplyAdapter adapter;
    private List<RecruitmentApply> applyList = new ArrayList<>();
    private Long recruitmentId; // 채용 공고 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruitment_apply_list);

        // Intent로부터 recruitmentId 받아오기
        Intent intent = getIntent();
        recruitmentId = intent.getLongExtra("recruitmentId", 0L); // 버튼 클릭 시 넘겨받은 recruitmentId

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecruitmentApplyAdapter(applyList, this);
        recyclerView.setAdapter(adapter);

        loadApplyList();
    }

    private void loadApplyList() {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);

        // API 호출
        recruitmentAPI.getRecruitmentApplyList(recruitmentId).enqueue(new Callback<SuccessResponse<GetRecruitmentApplyListResponseDto>>() {
            @Override
            public void onResponse(Call<SuccessResponse<GetRecruitmentApplyListResponseDto>> call, Response<SuccessResponse<GetRecruitmentApplyListResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 서버 응답에서 applyList 추출
                    List<RecruitmentApply> recruitmentApplyList = response.body().getData().getApplyList();
                    applyList.clear();
                    if (recruitmentApplyList != null) {
                        applyList.addAll(recruitmentApplyList);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(RecruitmentApplyListActivity.this, "데이터 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<GetRecruitmentApplyListResponseDto>> call, Throwable t) {
                Toast.makeText(RecruitmentApplyListActivity.this, "API 요청 실패", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "네트워크 오류: " + t.getMessage());
            }
        });
    }
}
