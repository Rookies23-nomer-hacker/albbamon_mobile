package com.example.albbamon;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class InquiriesMyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyInquiriesAdapter adapter;
    private List<InquiryItem> inquiryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inquiries); // 올바른 레이아웃 파일 사용

        // RecyclerView 설정
        recyclerView = findViewById(R.id.recyclerView); // `findViewById()` 사용
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 더미 데이터 추가
        loadDummyData();
    }

    private void loadDummyData() {
        inquiryList = new ArrayList<>();

        // 더미 데이터 생성
        inquiryList.add(new InquiryItem("회원정보", "회원가입 문의", "2024-02-19", "처리 중"));
        inquiryList.add(new InquiryItem("이력서관리", "이력서 수정 관련", "2024-02-18", "완료"));
        inquiryList.add(new InquiryItem("구직활동관리", "지원 현황 확인 문의", "2024-02-17", "처리 중"));
        inquiryList.add(new InquiryItem("유료서비스", "결제 오류 발생", "2024-02-16", "완료"));

        // 어댑터 설정
        adapter = new MyInquiriesAdapter(inquiryList);
        recyclerView.setAdapter(adapter);
    }
}
