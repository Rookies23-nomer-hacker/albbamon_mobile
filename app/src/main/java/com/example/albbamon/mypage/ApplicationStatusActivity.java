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
import com.example.albbamon.model.ApplyCountResponse;
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

/**
 * ✅ 지원현황 페이지 (UserMypageActivity에서 데이터를 전달받아 활용!)
 */
public class ApplicationStatusActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    private String applyCount; // ✅ UserMypageActivity에서 전달받은 지원서 개수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_status);

        // ✅ UserMypageActivity에서 전달된 데이터 받기
       applyCount = getIntent().getStringExtra("apply_count");

        // 툴바 제목 설정
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("지원현황");

        // 뒤로가기 버튼
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> onBackPressed());

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

        // ✅ 상위 탭 설정
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // ✅ 프래그먼트 추가
        fragments.add(new OnlineSupportFragment());
        titles.add("온라인·문자·이메일 지원");

        fragments.add(new OtherSupportFragment());
        titles.add("기타 지원");

        adapter = new ViewPagerAdapter(this, fragments, titles);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(adapter.getTabTitle(position));
            }
        }).attach();

        // ✅ UserMypageActivity에서 전달받은 데이터가 있으면 적용
        if (applyCount != null) {
            try {
                int count = Integer.parseInt(applyCount);
                updateOnlineSupportFragment(Integer.parseInt(applyCount));
            } catch (NumberFormatException e) {
                Log.e("applyCountError", "지원서 개수 변환 오류: " + e.getMessage());
            }
        }

        // ✅ API 호출하여 지원현황 데이터 가져오기
        fetchMyApplyCount();
    }

    /**
     * ✅ API 호출하여 지원현황 데이터를 가져오는 메서드
     */
    private void fetchMyApplyCount() {
        SupportStatusService apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(SupportStatusService.class);
        Call<ApplyCountResponse> call = apiService.getMyApplyCount();

        call.enqueue(new Callback<ApplyCountResponse>() {
            @Override
            public void onResponse(Call<ApplyCountResponse> call, Response<ApplyCountResponse> response) {
                Log.d("applyPageList", "API 호출 성공!");

                if (response.isSuccessful() && response.body() != null) {
                    ApplyCountResponse result = response.body();
                    try {
                        int count = Integer.parseInt(result.getData());

                        Log.d("API_RESPONSE", "status=" + result.getStatus()
                                + ", message=" + result.getMessage()
                                + ", applyCount=" + count);

                        // ✅ OnlineSupportFragment에도 데이터 전달
                        updateOnlineSupportFragment(count);
                    } catch (NumberFormatException e) {
                        Log.e("applyCountError", "API 응답 변환 오류: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR", "응답 실패: code=" + response.code()
                            + ", message=" + response.message());
                    Toast.makeText(ApplicationStatusActivity.this, "지원서 개수 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApplyCountResponse> call, Throwable t) {
                Log.e("API_ERROR", "API 호출 실패: " + t.getMessage());
                Toast.makeText(ApplicationStatusActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ✅ OnlineSupportFragment의 UI를 업데이트하는 메서드
     */
    private void updateOnlineSupportFragment(int count) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof OnlineSupportFragment) {
                ((OnlineSupportFragment) fragment).setTabNumber(0, count); // ✅ 전체(0) 탭 업데이트
                ((OnlineSupportFragment) fragment).setTabNumber(1, count); // ✅ 지원완료(1) 탭 업데이트
                Log.d("applyList", "setTabNumber() 실행됨, count=" + count);
                break; // ✅ 첫 번째 해당하는 프래그먼트만 업데이트 후 종료
            }
        }
    }
}
