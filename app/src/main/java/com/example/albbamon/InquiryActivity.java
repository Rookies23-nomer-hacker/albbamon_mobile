package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;

public class InquiryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry); // inquiry.xml을 사용

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // 현재 선택된 탭을 반영
        int currentTab = getIntent().getIntExtra("selected_tab", 0);
        TabLayout.Tab selectedTab = tabLayout.getTabAt(currentTab);
        if (selectedTab != null) {
            selectedTab.select();
        }

        // 탭 변경 시 Activity 전환
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 1) { // "나의 문의내역" 탭 선택 시
                    Intent intent = new Intent(InquiryActivity.this, InquiriesMyActivity.class);
                    intent.putExtra("selected_tab", 1);
                    startActivity(intent);
                    finish(); // 현재 액티비티 종료
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
