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
import com.example.albbamon.model.ApplyItem;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.network.SupportStatusService;
import com.example.albbamon.utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.albbamon.model.ApplyCountResponse;

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

    // 🔥 전달받은 지원 개수를 저장하는 변수
    private int receivedApplyCount = -1;

    // 하위 탭 제목
    private final String[] tabTitles = {"전체", "지원완료", "면접", "합격", "불합격"};

    /**
     * ✅ 새로운 인스턴스를 생성하면서 데이터를 전달할 수 있도록 처리
     */
    public static OnlineSupportFragment newInstance(int applyCount) {
        OnlineSupportFragment fragment = new OnlineSupportFragment();
        Bundle args = new Bundle();
        args.putInt("apply_count", applyCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_support, container, false);

        subTabLayout = view.findViewById(R.id.subTabLayout);
        subViewPager = view.findViewById(R.id.subViewPager);

        // 🔹 ViewPager2 오버스크롤 제거 (하단 스크롤 효과 제거)
        subViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // ✅ Bundle에서 `apply_count` 데이터 가져오기
        if (getArguments() != null) {
            receivedApplyCount = getArguments().getInt("apply_count", -1);
            Log.d("OnlineSupportFragment", "전달받은 applyCount: " + receivedApplyCount);
        }

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AllSupportFragment());
        fragments.add(new ApplyCompleteFragment());
        fragments.add(new InterviewFragment());
        fragments.add(new PassFragment());
        fragments.add(new FailFragment());

        // 🔹 어댑터 설정
        subAdapter = new ViewPagerAdapter(requireActivity(), fragments, Arrays.asList(tabTitles));
        subViewPager.setAdapter(subAdapter);

        setupTabLayout();

        // ✅ 전달된 데이터가 있으면 즉시 적용, 없으면 API 요청
        if (receivedApplyCount != -1) {
            setTabNumber(1, receivedApplyCount);
        } else {
            fetchSupportCounts();
        }

        return view;
    }

    /**
     * ✅ Retrofit을 통해 서버에서 지원현황 데이터를 받아와 탭 UI를 업데이트합니다.
     */
    private void fetchSupportCounts() {
        Log.d("DEBUG", "fetchSupportCounts() 호출됨-------");
        SupportStatusService apiService = RetrofitClient.getRetrofitInstanceWithSession(getContext()).create(SupportStatusService.class);
        Call<ApplyCountResponse> call = apiService.getMyApplyCount();

        call.enqueue(new Callback<ApplyCountResponse>() {
            @Override
            public void onResponse(Call<ApplyCountResponse> call, Response<ApplyCountResponse> response) {
                Log.d("applylast", "여기는 onResponse");
                if (response.isSuccessful() && response.body() != null) {
                    ApplyCountResponse counts = response.body();
                    Log.d("API_RESPONSE", "지원현황 데이터: " + counts.toString());

                    try {
                        int totalCount = Integer.parseInt(counts.getData()); // 전체 지원 개수
                        int applyCompleteCount = 0; // 지원완료 개수

                        // ✅ `getList()`가 null이 아니면 실행
                        for (ApplyItem item : counts.getList()) {
                            if ("지원완료".equals(item.getStatus())) {
                                applyCompleteCount++;
                            }
                        }

                        // ✅ UI 업데이트 (전체 지원 개수 및 지원완료 개수)
                        setTabNumber(0, totalCount);  // 전체 탭 업데이트
                        setTabNumber(1, applyCompleteCount); // 지원완료 탭 업데이트

                    } catch (NumberFormatException e) {
                        Log.e("applyCountError", "API 응답 변환 오류: " + e.getMessage());
                    }

                } else {
                    Log.e("API_ERROR", "응답 실패: code=" + response.code() + ", message=" + response.message());
                    Toast.makeText(getContext(), "지원현황 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApplyCountResponse> call, Throwable t) {
                Log.e("API_ERROR", "API 호출 실패: " + t.getMessage());
                Toast.makeText(getContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ✅ TabLayout과 ViewPager2를 연결하여 하위 탭을 구성합니다.
     */
    private void setupTabLayout() {
        Log.d("DEBUG", "setupTabLayout() 호출됨");
        new TabLayoutMediator(subTabLayout, subViewPager, (tab, position) -> {
            View customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null);
            TextView tabNumber = customView.findViewById(R.id.tab_number);
            TextView tabText = customView.findViewById(R.id.tab_text);

            tabNumber.setText("0");  // 기본값 설정
            tabText.setText(tabTitles[position]);

            int color = (position == 0) ? ContextCompat.getColor(requireContext(), R.color.appcolor)
                    : ContextCompat.getColor(requireContext(), android.R.color.black);
            tabNumber.setTextColor(color);
            tabText.setTextColor(color);

            tab.setCustomView(customView);
        }).attach();
    }

    /**
     * ✅ 특정 탭의 숫자를 업데이트하는 메서드
     */
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
