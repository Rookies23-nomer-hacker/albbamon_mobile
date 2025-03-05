package com.example.albbamon.mypage;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.albbamon.R;
import com.example.albbamon.dto.response.ApplyCountResponse;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.network.SupportStatusService;
import com.example.albbamon.utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationStatusActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_status);

        // 툴바 제목 설정
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("지원현황");


        findViewById(R.id.back).setOnClickListener(v -> finish()); // 현재 액티비티 종료


        // 검색 기능 설정
        EditText searchEditText = findViewById(R.id.searchEditText);
        ImageView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                Toast.makeText(this, "검색어: " + query, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 상위 탭 설정
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // OnlineSupportFragment 추가
        OnlineSupportFragment onlineSupportFragment = new OnlineSupportFragment();
        fragments.add(onlineSupportFragment);
        titles.add("온라인·문자·이메일 지원");

        // 기타 지원 탭 추가
        fragments.add(new OtherSupportFragment());
        titles.add("기타 지원");

        adapter = new ViewPagerAdapter(this, fragments, titles);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(adapter.getTabTitle(position));
        }).attach();

        // API 호출하여 지원현황 데이터 가져오기
        fetchMyApplyCount(onlineSupportFragment);
    }

    /**
     * API를 호출하여 지원 데이터 가져와서 OnlineSupportFragment에 전달
     */
    private void fetchMyApplyCount(OnlineSupportFragment onlineSupportFragment) {
        SupportStatusService apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(SupportStatusService.class);
        Call<ApplyCountResponse> call = apiService.getMyApplyCount();

        call.enqueue(new Callback<ApplyCountResponse>() {
            @Override
            public void onResponse(Call<ApplyCountResponse> call, Response<ApplyCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().getData();

                    Log.d("API_RESPONSE", "지원 개수 데이터: " + result);

                    // OnlineSupportFragment에 데이터 전달
                    onlineSupportFragment.updateTabData(result);
                } else {
                    Log.e("API_ERROR", "응답 실패: " + response.message());
                    Toast.makeText(ApplicationStatusActivity.this, "지원 개수 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApplyCountResponse> call, Throwable t) {
                Log.e("API_ERROR", "API 호출 실패: " + t.getMessage());
                Toast.makeText(ApplicationStatusActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
