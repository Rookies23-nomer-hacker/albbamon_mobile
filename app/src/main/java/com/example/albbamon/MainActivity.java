package com.example.albbamon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.api.PaymentAPI;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.mypage.UserMypageActivity;
import com.example.albbamon.network.RetrofitClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.reflect.TypeToken;




public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerSpecial, recyclerRecent, recyclerCommunity;
    private AppBarLayout appBarLayout;
    private BottomNavigationView bottomNavigationView;
    private Button btnMoreSpecial, btnMorePoint, btnMoreBrands;
    private static final int MAX_ITEMS = 5;
    private List<JobModel> allJobsSpecial, allJobsRecent, allJobsCommunity,displayedJobsCommunity;
    private JobAdapter jobAdapterSpecial, jobAdapterRecent, jobAdapterCommunity;
    private ViewPager2 viewPager;
    private TextView bannerIndicator;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int currentPage = 0;
    private final int AUTO_SCROLL_DELAY = 3000; // 3초마다 변경


    private CommunityAdapter adapter;
    private Button btnFetchPosts;
    private CommunityAPI apiService;


    private JobAdapter recruitmentAdapter;

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

    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1); // 기본값 -1 (로그인 안 된 상태)
        return userId != -1; // userId가 있으면 true (로그인 상태), 없으면 false (로그아웃 상태)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerSpecial = findViewById(R.id.recycler_special);
        recyclerRecent = findViewById(R.id.recycler_recent);
        recyclerCommunity = findViewById(R.id.recycler_community);
        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);

        // "더보기" 버튼 추가
        btnMoreSpecial = findViewById(R.id.btn_more_point1);
        btnMorePoint = findViewById(R.id.btn_more_point2);
        btnMoreBrands = findViewById(R.id.btn_more_point3);

        // 전체 데이터 목록
        allJobsSpecial = new ArrayList<>();
        allJobsRecent = new ArrayList<>();
        allJobsCommunity = new ArrayList<>();

        jobAdapterSpecial = new JobAdapter(allJobsSpecial);
        jobAdapterRecent = new JobAdapter(allJobsRecent);
        jobAdapterCommunity = new JobAdapter(displayedJobsCommunity);

        recyclerSpecial.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerSpecial.setAdapter(jobAdapterSpecial);

        recyclerRecent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerRecent.setAdapter(jobAdapterRecent);

        recyclerCommunity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCommunity.setAdapter(jobAdapterCommunity);


        // ✅ RecyclerView 아이템 클릭 이벤트 설정
        jobAdapterSpecial.setOnItemClickListener(position -> {
            JobModel clickedJob = allJobsSpecial.get(position);
            Toast.makeText(MainActivity.this, "클릭한 알바: " + clickedJob.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // ✅ 메뉴 버튼 클릭
        ImageView menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, 0);
        });



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

        apiService = RetrofitClient.getRetrofitInstance().create(CommunityAPI.class);

        recyclerCommunity = findViewById(R.id.recycler_community);
        allJobsCommunity = new ArrayList<>();
        displayedJobsCommunity = new ArrayList<>();

        jobAdapterCommunity = new JobAdapter(displayedJobsCommunity);
        recyclerCommunity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCommunity.setAdapter(jobAdapterCommunity);

        recruitmentAdapter = new JobAdapter(allJobsRecent);
        recyclerRecent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerRecent.setAdapter(recruitmentAdapter);

        fetchCommunityPosts();
        fetchRecruitmentPosts();
        fetchPremiumRecruitmentPosts();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                if (isUserLoggedIn()) {
                    // ✅ 로그인 상태면 마이페이지로 이동
                    Intent intent = new Intent(MainActivity.this, UserMypageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, 0);
                } else {
                    // ✅ 로그인 안 되어 있으면 로그인 화면으로 이동
                    Intent intent = new Intent(MainActivity.this, SignIn.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, 0);
                }
                return true;
            }

            return false;
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
                    allJobsCommunity.clear();

                    for (CommunityModel post : response.body()) {
                        // 🔥 file 값이 NULL이면 기본 이미지 사용
                        String imageUrl = (post.getFile_name() == null || post.getFile_name().isEmpty())
                                ? null  // NULL일 경우 기본 이미지 사용
                                : "서버_URL/" + post.getFile_name(); // 서버 URL을 붙여서 사용

                        // 🔥 커뮤니티 전용 생성자 사용
                        allJobsCommunity.add(new JobModel(
                                post.getTitle(),
                                "작성자: " + post.getUserName(),  // subtitle (작성자)
                                imageUrl,
                                true // 커뮤니티 데이터임을 명시
                        ));
                    }

                    displayedJobsCommunity.clear();
                    displayedJobsCommunity.addAll(allJobsCommunity.subList(0, Math.min(MAX_ITEMS, allJobsCommunity.size())));

                    if (jobAdapterCommunity == null) {
                        jobAdapterCommunity = new JobAdapter(displayedJobsCommunity);
                        recyclerCommunity.setAdapter(jobAdapterCommunity);
                    } else {
                        jobAdapterCommunity.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Community 데이터 로딩 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CommunityModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Community API 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecruitmentPosts() {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstance().create(RecruitmentAPI.class);
        Call<RecruitmentResponse> call = recruitmentAPI.getRecruitmentPosts();

        call.enqueue(new Callback<RecruitmentResponse>() {
            @Override
            public void onResponse(Call<RecruitmentResponse> call, Response<RecruitmentResponse> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    RecruitmentResponse recruitmentResponse = response.body();
                    Log.d("API_RESPONSE", "Message: " + recruitmentResponse.getMessage());

                    allJobsRecent.clear();

                    // ✅ "recruitmentList" 내부의 데이터를 가져옴 (최대 5개만)
                    if (recruitmentResponse.getData() != null && recruitmentResponse.getData().getRecruitmentList() != null) {
                        List<RecruitmentModel> jobList = recruitmentResponse.getData().getRecruitmentList();

                        // 🔥 최대 5개까지만 가져오기
                        int maxItems = Math.min(jobList.size(), 5);
                        for (int i = 0; i < maxItems; i++) {
                            RecruitmentModel job = jobList.get(i);

                            String imageUrl = (job.getFile() == null || job.getFile().isEmpty())
                                    ? null
                                    : "서버_URL/" + job.getFile();

                            allJobsRecent.add(new JobModel(
                                    job.getTitle(),
                                    (job.getWage() != null) ? "급여: " + job.getWage() : "급여 정보 없음",
                                    imageUrl
                            ));
                        }
                    } else {
                        Log.e("API_ERROR", "recruitmentList가 비어 있음.");
                        Toast.makeText(MainActivity.this, "채용 공고 데이터 없음", Toast.LENGTH_SHORT).show();
                    }

                    recruitmentAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API_ERROR", "Response Failed. Body: " + response.errorBody());
                    Toast.makeText(MainActivity.this, "채용 공고 데이터 로딩 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecruitmentResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "채용 공고 API 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPremiumRecruitmentPosts() {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstance().create(RecruitmentAPI.class);
        Call<RecruitmentResponse> recruitmentCall = recruitmentAPI.getRecruitmentPosts();
        Call<List<Long>> paymentCall = RetrofitClient.getRetrofitInstance().create(PaymentAPI.class).getPremiumUserIds(); // ✅ 결제된 유저 ID 가져오기

        // 1️⃣ 모든 채용 공고 가져오기
        recruitmentCall.enqueue(new Callback<RecruitmentResponse>() {
            @Override
            public void onResponse(Call<RecruitmentResponse> call, Response<RecruitmentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecruitmentResponse recruitmentResponse = response.body();
                    List<RecruitmentModel> allJobs = recruitmentResponse.getData().getRecruitmentList();
                    Log.d("API_RESPONSE", "Total Jobs Count: " + allJobs.size()); // 🔥 총 공고 개수 확인

                    // 2️⃣ 결제된 유저 ID 가져오기
                    paymentCall.enqueue(new Callback<List<Long>>() {
                        @Override
                        public void onResponse(Call<List<Long>> call, Response<List<Long>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Long> premiumUserIds = response.body();
                                Log.d("API_RESPONSE", "Premium User IDs: " + premiumUserIds); // 🔥 프리미엄 유저 ID 리스트 확인

                                allJobsSpecial.clear();

                                // 3️⃣ 채용 공고 중 `user_id`가 결제된 유저 ID에 포함된 것만 필터링
                                for (RecruitmentModel job : allJobs) {
                                    Log.d("API_RESPONSE", "Checking Job: " + job.getTitle() + " | User ID: " + job.getUserId());

                                    if (premiumUserIds.contains(job.getUserId())) {
                                        Log.d("API_RESPONSE", "✅ Premium Job Found: " + job.getTitle());

                                        String imageUrl = (job.getFile() == null || job.getFile().isEmpty())
                                                ? null
                                                : "서버_URL/" + job.getFile();

                                        allJobsSpecial.add(new JobModel(
                                                job.getTitle(),
                                                (job.getWage() != null) ? "급여: " + job.getWage() : "급여 정보 없음",
                                                imageUrl
                                        ));

                                        // ✅ 최대 5개까지만 가져오기
                                        if (allJobsSpecial.size() >= 5) break;
                                    }
                                }

                                Log.d("API_RESPONSE", "Final Premium Job Count: " + allJobsSpecial.size()); // 🔥 최종 프리미엄 공고 개수 확인
                                jobAdapterSpecial.notifyDataSetChanged(); // ✅ RecyclerView 갱신
                            } else {
                                Log.e("API_ERROR", "프리미엄 유저 ID 데이터 없음.");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Long>> call, Throwable t) {
                            Log.e("API_FAILURE", "결제된 유저 ID API 요청 실패: " + t.getMessage());
                        }
                    });
                } else {
                    Log.e("API_ERROR", "채용 공고 데이터 없음.");
                }
            }

            @Override
            public void onFailure(Call<RecruitmentResponse> call, Throwable t) {
                Log.e("API_FAILURE", "채용 공고 API 요청 실패: " + t.getMessage());
            }
        });
    }


}
