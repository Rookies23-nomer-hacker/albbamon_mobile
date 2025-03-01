package com.example.albbamon.mypage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.example.albbamon.adapter.MyRecruitmentAdapter;
import com.example.albbamon.model.MyRecruitment;
import com.example.albbamon.dto.response.GetRecruitmentResponseDto;
import com.example.albbamon.model.SuccessResponse;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.network.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecruitmentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyRecruitmentAdapter adapter;
    private RecruitmentAPI recruitmentAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recruitment_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Retrofit을 이용한 API 객체 생성
        recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);

        // ✅ 채용 공고 목록 불러오기
        loadMyRecruitmentList();
    }

    private void loadMyRecruitmentList() {
        recruitmentAPI.getMyRecruitments().enqueue(new Callback<SuccessResponse<GetRecruitmentResponseDto>>() {
            @Override
            public void onResponse(Call<SuccessResponse<GetRecruitmentResponseDto>> call, Response<SuccessResponse<GetRecruitmentResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetRecruitmentResponseDto recruitmentData = response.body().getData(); // ✅ `data` 필드 추출
                    if (recruitmentData != null && recruitmentData.getRecruitmentList() != null) {
                        List<MyRecruitment> recruitmentList = recruitmentData.getRecruitmentList(); // ✅ 실제 리스트 추출
                        adapter = new MyRecruitmentAdapter(recruitmentList, MyRecruitmentListActivity.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(MyRecruitmentListActivity.this, "채용 공고가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyRecruitmentListActivity.this, "데이터 불러오기 실패", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "서버 응답 오류: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse<GetRecruitmentResponseDto>> call, Throwable t) {
                Toast.makeText(MyRecruitmentListActivity.this, "API 요청 실패", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "네트워크 오류: " + t.getMessage());
            }
        });
    }
}
