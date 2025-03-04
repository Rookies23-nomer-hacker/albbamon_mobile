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
 * 지원현황 페이지
 * 상위 탭:
 *   1) 온라인·문자·이메일 지원 (OnlineSupportFragment)
 *   2) 기타 지원 (OtherSupportFragment)
 *
 * API를 통해 지원현황 데이터를 받아오는 로직 유지
 * 필요 없는 UI 요소 제거
 */
public class ApplicationStatusActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    List<Fragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_status);

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

        // 상위 탭 설정
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);



        // 1) 온라인·문자·이메일 지원 탭 → OnlineSupportFragment
        fragments.add(new OnlineSupportFragment());
        titles.add("온라인·문자·이메일 지원");

        // 2) 기타 지원 탭 → OtherSupportFragment
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

        // API 호출하여 지원현황 데이터 가져오기
        fetchMyApplyCount();

    }

    private void fetchMyApplyCount() {
        SupportStatusService apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(SupportStatusService.class);
        Call<ApplyCountResponse> call = apiService.getMyApplyCount();
        call.enqueue(new Callback<ApplyCountResponse>() {
            @Override
            public void onResponse(Call<ApplyCountResponse> call, Response<ApplyCountResponse> response) {

                Log.d("applyPageList", "여기까지 오니");
                Log.d("applyPageList", response.message());
                Log.d("applyPageList", String.valueOf(response.body()));
                Log.d("applyPageList", response.body().getData());
                Log.d("applyPageList", response.body().getStatus());
                Log.d("applyPageList", response.body().getMessage());

                if (response.isSuccessful() && response.body() != null) {
                    ApplyCountResponse result = response.body();
                    Log.d("API_RESPONSE", "status=" + result.getStatus()
                            + ", message=" + result.getMessage()
                            + ", applyCount=" + result.getData());
                    // 예: UI 업데이트 (예: TextView에 지원서 개수 표시)
                    TextView tabNumber = findViewById(R.id.tab_number);
                    Log.d("tabNumber", String.valueOf(tabNumber));

                    Integer count = Integer.valueOf(response.body().getData());

//                    OnlineSupportFragment onlineSupportFragment = new OnlineSupportFragment();
//                    if (onlineSupportFragment != null) {
//                        onlineSupportFragment.setTabNumber(1, count);
//                    }

                    for (Fragment fragment : fragments) {
                        if (fragment instanceof OnlineSupportFragment) {
                            ((OnlineSupportFragment) fragment).setTabNumber(1, count);
                            Log.d("applyList", "여기로 tabnumber가 넘어오니?");
                            break; // 찾으면 종료
                        }
                    }

//                    tvApplyCount.setText(String.valueOf(result.getData()));
                } else {
                    Log.e("API_ERROR", "응답 실패: code=" + response.code()
                            + ", message=" + response.message());
                    Toast.makeText(ApplicationStatusActivity.this, "지원서 개수 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApplyCountResponse> call, Throwable t) {
                Log.e("API_ERROR", "여기까지 오니: " + t.getMessage());
                Log.e("API_ERROR", "API 호출 실패: " + t.getMessage());
                Toast.makeText(ApplicationStatusActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

