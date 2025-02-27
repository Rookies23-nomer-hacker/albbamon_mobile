package com.example.albbamon;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private LinearLayout recentSearchContainer;
    private GridLayout recommendedSearchContainer;
    private GridLayout popularSearchContainer;

    private List<String> recentSearches = new ArrayList<>(Arrays.asList("정보보안", "페스티벌", "전산직", "숙박"));
    private List<String> recommendedSearches = new ArrayList<>(Arrays.asList("공조냉동", "네트워크보안", "치위생사", "사용성", "특수경비", "팬미팅", "시사책", "콘서트", "진행스텝", "행사장초"));
    private List<String> popularSearches = new ArrayList<>(Arrays.asList("카페", "편의점", "주말", "야간", "당일지급", "독서실", "사무보조", "단기", "학원", "재택"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // XML 요소 연결
        searchView = findViewById(R.id.searchView);
        recentSearchContainer = findViewById(R.id.recentSearchContainer);
        recommendedSearchContainer = findViewById(R.id.recommendedSearchContainer);
        popularSearchContainer = findViewById(R.id.popularSearchContainer);

        // 검색어 목록 표시
        populateSearchTags(recentSearches, recentSearchContainer);
        populateSearchTags(recommendedSearches, recommendedSearchContainer);
        populateSearchTags(popularSearches, popularSearchContainer);

        // 검색 이벤트 설정
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    addRecentSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // 태그를 동적으로 추가하는 함수
    private void populateSearchTags(List<String> tags, View container) {
        if (container instanceof LinearLayout) {
            ((LinearLayout) container).removeAllViews();
            for (String tag : tags) {
                TextView textView = createTagView(tag);
                ((LinearLayout) container).addView(textView);
            }
        } else if (container instanceof GridLayout) {
            ((GridLayout) container).removeAllViews();
            for (String tag : tags) {
                TextView textView = createTagView(tag);
                ((GridLayout) container).addView(textView);
            }
        }
    }

    // 검색 태그 UI 생성
    private TextView createTagView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 8, 16, 8);
        textView.setBackgroundResource(R.drawable.tag_background);
        textView.setOnClickListener(v -> Toast.makeText(SearchActivity.this, text + " 선택됨", Toast.LENGTH_SHORT).show());
        return textView;
    }

    // 새로운 검색어 추가
    private void addRecentSearch(String query) {
        if (!recentSearches.contains(query)) {
            recentSearches.add(0, query);
            if (recentSearches.size() > 5) {
                recentSearches.remove(recentSearches.size() - 1);
            }
            populateSearchTags(recentSearches, recentSearchContainer);
        }
    }
}
