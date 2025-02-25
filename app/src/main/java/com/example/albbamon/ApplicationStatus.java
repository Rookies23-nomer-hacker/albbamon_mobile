package com.example.albbamon;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ApplicationStatus extends AppCompatActivity {

    private View indicatorOnline, indicatorOther;
    private EditText searchEditText;
    private ViewSwitcher viewSwitcher;
    private LinearLayout tabOnlineContainer, tabOtherContainer;
    private LinearLayout contentOnline, contentOther;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_status);

        // 툴바 제목 및 뒤로가기 버튼
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("지원현황");

        ImageView backButton = findViewById(R.id.back);
        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ApplicationStatus.this, UserMypage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 검색 기능
        searchEditText = findViewById(R.id.searchEditText);
        ImageView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Toast.makeText(this, "검색어: " + query, Toast.LENGTH_SHORT).show();
                // TODO: 검색 기능 추가 (API 호출 또는 필터링 처리)
            } else {
                Toast.makeText(this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        // 🔹 탭 목록과 프래그먼트 동적 추가 가능
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        fragments.add(new OnlineSupportFragment());
        titles.add("온라인·문자·이메일 지원");

        fragments.add(new OtherSupportFragment());
        titles.add("기타 지원");

        // 🔹 어댑터 설정
        adapter = new ViewPagerAdapter(this, fragments, titles);
        viewPager.setAdapter(adapter);

        // 🔹 TabLayout + ViewPager2 연결
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(adapter.getTabTitle(position));
            }
        }).attach();

    }
}
