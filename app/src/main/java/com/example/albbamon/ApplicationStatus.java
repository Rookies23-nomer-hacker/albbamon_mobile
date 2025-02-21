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

import androidx.appcompat.app.AppCompatActivity;

public class ApplicationStatus extends AppCompatActivity {

    private TextView tabOnline, tabOther, toolbarTitle;
    private View indicatorOnline, indicatorOther;
    private EditText searchEditText;
    private ImageView searchIcon, backButton;
    private ViewSwitcher viewSwitcher;
    private LinearLayout tabOnlineContainer, tabOtherContainer;
    private LinearLayout contentOnline, contentOther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_status);

        // 툴바 제목 및 뒤로가기 버튼
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("지원현황");

        backButton = findViewById(R.id.back);
        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ApplicationStatus.this, UserMypage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        // 검색 기능
        searchEditText = findViewById(R.id.searchEditText);
        searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Toast.makeText(this, "검색어: " + query, Toast.LENGTH_SHORT).show();
                // TODO: 검색 기능 추가 (API 호출 또는 필터링 처리)
            } else {
                Toast.makeText(this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 탭 전환 요소 초기화
        tabOnline = findViewById(R.id.tab_online);
        tabOther = findViewById(R.id.tab_other);
        indicatorOnline = findViewById(R.id.indicator_online);
        indicatorOther = findViewById(R.id.indicator_other);
        tabOnlineContainer = findViewById(R.id.tab_online_container);
        tabOtherContainer = findViewById(R.id.tab_other_container);
        contentOnline = findViewById(R.id.content_online);
        contentOther = findViewById(R.id.content_other);

        // 초기 탭 설정 (온라인·문자·이메일 지원 활성화)
        setActiveTab(true);

        // 온라인·문자·이메일 지원 탭 클릭
        tabOnlineContainer.setOnClickListener(v -> {
            if (viewSwitcher.getDisplayedChild() != 0) {
                viewSwitcher.showPrevious();
            }
            setActiveTab(true);
        });

        // 기타 지원 탭 클릭
        tabOtherContainer.setOnClickListener(v -> {
            if (viewSwitcher.getDisplayedChild() != 1) {
                viewSwitcher.showNext();
            }
            setActiveTab(false);
        });
    }

    // 선택된 탭 활성화
    private void setActiveTab(boolean isOnline) {
        if (isOnline) {
            tabOnline.setTextColor(getResources().getColor(R.color.black));
            tabOnline.setTypeface(null, Typeface.BOLD);
            indicatorOnline.setBackgroundColor(getResources().getColor(R.color.appcolor));

            tabOther.setTextColor(getResources().getColor(R.color.gray));
            tabOther.setTypeface(null, Typeface.NORMAL);
            indicatorOther.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            tabOther.setTextColor(getResources().getColor(R.color.black));
            tabOther.setTypeface(null, Typeface.BOLD);
            indicatorOther.setBackgroundColor(getResources().getColor(R.color.appcolor));

            tabOnline.setTextColor(getResources().getColor(R.color.gray));
            tabOnline.setTypeface(null, Typeface.NORMAL);
            indicatorOnline.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }
}
