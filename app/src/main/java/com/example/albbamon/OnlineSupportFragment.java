package com.example.albbamon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnlineSupportFragment extends Fragment {
    private TabLayout subTabLayout;
    private ViewPager2 subViewPager;
    private ViewPagerAdapter subAdapter;

    private final int[] tabNumbers = {6, 6, 0, 0, 0}; // 각 탭의 숫자 값
    private final String[] tabTitles = {"전체", "지원완료", "면접", "합격", "불합격/취소"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_support, container, false);

        subTabLayout = view.findViewById(R.id.subTabLayout);
        subViewPager = view.findViewById(R.id.subViewPager);

        // 🔹 ViewPager2 오버스크롤 제거 (하단 스크롤 효과 제거)
        subViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // 🔹 프래그먼트 리스트 생성
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AllSupportFragment());
        fragments.add(new ApplyCompleteFragment());
        fragments.add(new InterviewFragment());
        fragments.add(new PassFragment());
        fragments.add(new FailFragment());

        // 🔹 어댑터 설정
        subAdapter = new ViewPagerAdapter(requireActivity(), fragments, Arrays.asList(tabTitles));
        subViewPager.setAdapter(subAdapter);

        // 🔹 TabLayout + ViewPager2 연결 + 커스텀 탭 적용
        new TabLayoutMediator(subTabLayout, subViewPager, (tab, position) -> {
            View customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null);
            TextView tabNumber = customView.findViewById(R.id.tab_number);
            TextView tabText = customView.findViewById(R.id.tab_text);

            tabNumber.setText(String.valueOf(tabNumbers[position])); // 숫자 설정
            tabText.setText(tabTitles[position]); // 제목 설정

            // 처음에는 "전체" 탭만 appcolor 적용
            int color = (position == 0) ? ContextCompat.getColor(requireContext(), R.color.appcolor) : ContextCompat.getColor(requireContext(), android.R.color.black);
            tabNumber.setTextColor(color);
            tabText.setTextColor(color);

            tab.setCustomView(customView);
        }).attach();

        // 🔹 탭 선택 리스너 (색상 변경)
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
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 탭이 다시 선택될 때의 동작 (필요 없으면 비워둠)
            }
        });

        return view;
    }
}
