package com.example.albbamon;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class MyInquiriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyInquiriesAdapter adapter;
    private List<InquiryItem> inquiryList;

    public MyInquiriesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_inquiries, container, false);

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 더미 데이터 추가
        loadDummyData();

        return view;
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


