package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.network.RetrofitClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerSpecial, recyclerPoint, recyclerBrands;
    private AppBarLayout appBarLayout;
    private BottomNavigationView bottomNavigationView;
    private Button btnMoreSpecial, btnMorePoint, btnMoreBrands;
    private static final int MAX_ITEMS = 5;
    private List<JobModel> allJobsSpecial, allJobsPoint, allJobsBrands;
    private JobAdapter jobAdapterSpecial, jobAdapterPoint, jobAdapterBrands;
    private ViewPager2 viewPager;
    private TextView bannerIndicator;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int currentPage = 0;
    private final int AUTO_SCROLL_DELAY = 3000; // 3초마다 변경


    private RecyclerView recyclerPosts;
    private CommunityAdapter adapter;
    private Button btnFetchPosts;
    private CommunityAPI apiService;


    private List<Integer> bannerImages = Arrays.asList(
            R.drawable.img_alrimi,
            R.drawable.b_logo,
            R.drawable.b_logo,
            R.drawable.b_logo,
            R.drawable.b_logo
    );

    private Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentPage == bannerImages.size()) {
                currentPage = 0; // 처음으로 되돌리기
            }
            viewPager.setCurrentItem(currentPage++, true);
            handler.postDelayed(this, AUTO_SCROLL_DELAY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerSpecial = findViewById(R.id.recycler_special);
        recyclerPoint = findViewById(R.id.recycler_point);
        recyclerBrands = findViewById(R.id.recycler_brands);
        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);

        // "더보기" 버튼 추가
        btnMoreSpecial = findViewById(R.id.btn_more_point1);
        btnMorePoint = findViewById(R.id.btn_more_point2);
        btnMoreBrands = findViewById(R.id.btn_more_point3);

        // 전체 데이터 목록
        allJobsSpecial = new ArrayList<>();
        allJobsPoint = new ArrayList<>();
        allJobsBrands = new ArrayList<>();

        // 샘플 데이터 추가
        for (int i = 1; i <= 10; i++) {
            allJobsSpecial.add(new JobModel("서빙 알바 " + i, "서브웨이", "시급 11,000원", R.drawable.sample_job));
            allJobsPoint.add(new JobModel("편의점 알바 " + i, "GS25", "시급 10,500원", R.drawable.sample_job));
            allJobsBrands.add(new JobModel("브랜드 알바 " + i, "스타벅스", "시급 12,000원", R.drawable.sample_job));
        }

        // 최대 5개만 보여주는 리스트 생성
        List<JobModel> displayedJobsSpecial = new ArrayList<>(allJobsSpecial.subList(0, Math.min(MAX_ITEMS, allJobsSpecial.size())));
        List<JobModel> displayedJobsPoint = new ArrayList<>(allJobsPoint.subList(0, Math.min(MAX_ITEMS, allJobsPoint.size())));
        List<JobModel> displayedJobsBrands = new ArrayList<>(allJobsBrands.subList(0, Math.min(MAX_ITEMS, allJobsBrands.size())));

        // 어댑터 설정
        jobAdapterSpecial = new JobAdapter(displayedJobsSpecial);
        jobAdapterPoint = new JobAdapter(displayedJobsPoint);
        jobAdapterBrands = new JobAdapter(displayedJobsBrands);

        recyclerSpecial.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerSpecial.setAdapter(jobAdapterSpecial);

        recyclerPoint.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerPoint.setAdapter(jobAdapterPoint);

        recyclerBrands.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerBrands.setAdapter(jobAdapterBrands);

        // ✅ RecyclerView 아이템 클릭 이벤트 설정
        jobAdapterSpecial.setOnItemClickListener(position -> {
            JobModel clickedJob = allJobsSpecial.get(position);
            Toast.makeText(MainActivity.this, "클릭한 알바: " + clickedJob.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // ✅ 로고 버튼 클릭
        ImageView logo = findViewById(R.id.icon);
        logo.setOnClickListener(v -> Toast.makeText(MainActivity.this, "로고 클릭됨!", Toast.LENGTH_SHORT).show());

        // ✅ 메뉴 버튼 클릭
        ImageView menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });





        // ✅ "제트" 버튼 클릭
        ImageView button1 = findViewById(R.id.button1_icon);
        button1.setOnClickListener(v -> Toast.makeText(MainActivity.this, "제트 버튼 클릭됨!", Toast.LENGTH_SHORT).show());

        // ✅ 스크롤 이벤트 감지하여 상단바 & 하단바 숨김 처리
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                appBarLayout.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                appBarLayout.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        // ✅ "더보기" 버튼 클릭 이벤트 (현재 기능 없음)
        btnMoreSpecial.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Special 더보기 클릭!", Toast.LENGTH_SHORT).show());
        btnMorePoint.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Point 더보기 클릭!", Toast.LENGTH_SHORT).show());
        btnMoreBrands.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Brands 더보기 클릭!", Toast.LENGTH_SHORT).show());

        viewPager = findViewById(R.id.banner_viewpager);
        bannerIndicator = findViewById(R.id.banner_indicator);

        // 배너 어댑터 설정
        BannerAdapter adapter = new BannerAdapter(this, bannerImages);
        viewPager.setAdapter(adapter);

        // 페이지 변경 이벤트 리스너
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bannerIndicator.setText((position + 1) + "/" + bannerImages.size());
            }
        });

        // 자동 슬라이드 시작
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);

        recyclerPosts = findViewById(R.id.recycler_posts);
        btnFetchPosts = findViewById(R.id.btn_fetch_posts);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getRetrofitInstance().create(CommunityAPI.class);

        btnFetchPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCommunityPosts();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoScrollRunnable); // 메모리 누수 방지
    }

    private void fetchCommunityPosts() {
        Call<List<CommunityModel>> call = apiService.getPosts();

        call.enqueue(new Callback<List<CommunityModel>>() {
            @Override
            public void onResponse(Call<List<CommunityModel>> call, Response<List<CommunityModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CommunityModel> posts = response.body();
                    adapter = new CommunityAdapter(posts);
                    recyclerPosts.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "서버 응답 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CommunityModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API 요청 실패", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }


}
