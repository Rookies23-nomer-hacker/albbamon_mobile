package com.example.albbamon.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.albbamon.ApplyCompleteFragment;
import com.example.albbamon.FailFragment;
import com.example.albbamon.InterviewFragment;
import com.example.albbamon.PassFragment;
import com.example.albbamon.R;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.network.SupportStatusService;
import com.example.albbamon.utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.albbamon.model.ApplyCountResponse;  // 수정된 부분

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineSupportFragment extends Fragment {

    private TabLayout subTabLayout;
    private ViewPager2 subViewPager;
    private ViewPagerAdapter subAdapter;

    // 하위 탭에 사용될 숫자 배열 및 제목
    private int[] tabNumbers = {0, 0, 0, 0, 0};
    private final String[] tabTitles = {"전체", "지원완료", "면접", "합격", "불합격/취소"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("DEBUG", "OnlineSupportFragment onCreateView 호출됨");
        View view = inflater.inflate(R.layout.fragment_online_support, container, false);

        subTabLayout = view.findViewById(R.id.subTabLayout);
        subViewPager = view.findViewById(R.id.subViewPager);
        subViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AllSupportFragment());
        fragments.add(new ApplyCompleteFragment());
        fragments.add(new InterviewFragment());
        fragments.add(new PassFragment());
        fragments.add(new FailFragment());

        subAdapter = new ViewPagerAdapter(requireActivity(), fragments, Arrays.asList(tabTitles));
        subViewPager.setAdapter(subAdapter);

        Log.d("applylast", "setupTabLayout() 실행될 차례");

        setupTabLayout();
        Log.d("applylast", "fetchSupportCounts 실행될 차례");
        fetchSupportCounts();
        Log.d("applylast", "fetchSupportCounts 실행된 후");

        return view;
    }

    /**
     * Retrofit을 통해 서버에서 지원현황 데이터를 받아와 탭 숫자 배열을 업데이트합니다.
     */
    private void fetchSupportCounts() {
        Log.d("DEBUG", "fetchSupportCounts() 호출됨-------");
        SupportStatusService apiService = RetrofitClient.getRetrofitInstanceWithSession(getContext()).create(SupportStatusService.class);
        Call<ApplyCountResponse> call = apiService.getMyApplyCount();
        call.enqueue(new Callback<ApplyCountResponse>() {
            @Override
            public void onResponse(Call<ApplyCountResponse> call, Response<ApplyCountResponse> response) {
                Log.d("applylast", "여기는 onresponse");
                if (response.isSuccessful() && response.body() != null) {
                    ApplyCountResponse counts = response.body();
                    Log.d("API_RESPONSE", "지원현황 데이터: " + counts.toString());

                    // getData() 사용 안 하고 count 값 직접 사용
//                    tabNumbers[0] = counts.getCount();

                    // 탭 UI를 갱신합니다.
                    setupTabLayout();
                } else {
                    Log.e("API_ERROR", "응답 실패: code=" + response.code() +
                            ", message=" + response.message());
                    Toast.makeText(getContext(), "지원현황 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApplyCountResponse> call, Throwable t) {
                Log.d("applylast", "여기는 onfail");

                Log.e("API_ERROR", "API 호출 실패: " + t.getMessage());
                Toast.makeText(getContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * TabLayout과 ViewPager2를 연결하여 하위 탭을 구성합니다.
     */
    private void setupTabLayout() {
        Log.d("DEBUG", "setupTabLayout() 호출됨");
        new TabLayoutMediator(subTabLayout, subViewPager, (tab, position) -> {
            View customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null);
            TextView tabNumber = customView.findViewById(R.id.tab_number);
            TextView tabText = customView.findViewById(R.id.tab_text);

            tabNumber.setText(String.valueOf(tabNumbers[position]));
            tabText.setText(tabTitles[position]);

            int color = (position == 0)
                    ? ContextCompat.getColor(requireContext(), R.color.appcolor)
                    : ContextCompat.getColor(requireContext(), android.R.color.black);
            tabNumber.setTextColor(color);
            tabText.setTextColor(color);

            tab.setCustomView(customView);
            Log.d("DEBUG", "Tab " + position + " 설정됨: 숫자 = " + tabNumbers[position] + ", 제목 = " + tabTitles[position]);
        }).attach();

        subTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabNumber = customView.findViewById(R.id.tab_number);
                    TextView tabText = customView.findViewById(R.id.tab_text);
                    int color = ContextCompat.getColor(requireContext(), R.color.appcolor);
                    tabNumber.setTextColor(color);
                    tabText.setTextColor(color);
                    Log.d("DEBUG", "Tab 선택됨: " + tabText.getText());
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null) {
                    TextView tabNumber = customView.findViewById(R.id.tab_number);
                    TextView tabText = customView.findViewById(R.id.tab_text);
                    int color = ContextCompat.getColor(requireContext(), android.R.color.black);
                    tabNumber.setTextColor(color);
                    tabText.setTextColor(color);
                    Log.d("DEBUG", "Tab 선택 해제됨: " + tabText.getText());
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("DEBUG", "Tab 재선택됨");
            }
        });
    }
    public void setTabNumber(int position, int value) {
        if (subTabLayout == null || subTabLayout.getTabAt(position) == null) {
            Log.e("setTabNumber", "subTabLayout 또는 해당 position의 탭이 초기화되지 않음.");
            return;
        }

        TabLayout.Tab tab = subTabLayout.getTabAt(position);
        View customView = (tab != null) ? tab.getCustomView() : null;

        if (customView == null) {
            // 기존 CustomView가 없는 경우 새로 생성 후 설정
            customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null);
            tab.setCustomView(customView);
        }

        TextView tabNumber = customView.findViewById(R.id.tab_number);
        if (tabNumber != null) {
            tabNumber.setText(String.valueOf(value));
            Log.d("setTabNumber", "탭 번호 업데이트됨: " + position + " -> " + value);
        }


        // UI 갱신을 위해 강제 업데이트
        tab.setCustomView(null); // 기존 CustomView를 제거
        tab.setCustomView(customView); // 새로운 CustomView를 설정
    }


}
