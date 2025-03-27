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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.viewpager2.widget.ViewPager2;

import com.example.albbamon.Experience.ExperienceList;
import com.example.albbamon.Experience.ExperienceView;
import com.example.albbamon.Resume.ResumeNewJobActivity;
import com.example.albbamon.Resume.ResumePremiumActivity;
import com.example.albbamon.aesbox.aesUtil;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.api.PostListResponse;
import com.example.albbamon.api.RecruitmentAPI;
import com.example.albbamon.api.ResponseWrapper;
import com.example.albbamon.dto.response.GetRecruitmentApplyListResponseDto;
import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.model.RecruitmentModel;
import com.example.albbamon.model.RecruitmentResponse;
import com.example.albbamon.mypage.UserMypageActivity;
import com.example.albbamon.mypage.CeoMypageActivity;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.repository.UserRepository;
import com.example.albbamon.sign.SignInActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerSpecial, recyclerRecent, recyclerCommunity;
    private AppBarLayout appBarLayout;
    private BottomNavigationView bottomNavigationView;
    private Button btnMoreSpecial, btnMoreRecent, btnMoreCommunity;
    private static final int MAX_ITEMS = 5;
    private List<JobModel> allJobsSpecial, allJobsRecent, allJobsCommunity;
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
            R.drawable.banner2,
            R.drawable.banner1,
            R.drawable.banner1,
            R.drawable.banner1
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
        btnMoreRecent = findViewById(R.id.btn_more_point2);
        btnMoreCommunity = findViewById(R.id.btn_more_point3);

        // 전체 데이터 목록
        allJobsSpecial = new ArrayList<>();
        allJobsRecent = new ArrayList<>();
        allJobsCommunity = new ArrayList<>();

        jobAdapterSpecial = new JobAdapter(allJobsSpecial);
        jobAdapterRecent = new JobAdapter(allJobsRecent);
        jobAdapterCommunity = new JobAdapter(allJobsCommunity);

        recyclerSpecial.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerSpecial.setAdapter(jobAdapterSpecial);

        recyclerRecent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerRecent.setAdapter(jobAdapterRecent);

        recyclerCommunity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCommunity.setAdapter(jobAdapterCommunity);


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

        // ✅ RecyclerView 아이템 클릭 이벤트 설정
        jobAdapterSpecial.setOnItemClickListener(position -> {
            JobModel clickedJob = allJobsSpecial.get(position);

            // ✅ 선택한 공고 ID 가져오기
            Long jobId = clickedJob.getId();

            // ✅ Intent를 통해 RecruitmentViewActivity로 ID 전달
            Intent intent = new Intent(MainActivity.this, RecruitmentViewActivity.class);
            intent.putExtra("job_id", jobId);
            startActivity(intent);
        });

        // ✅ RecyclerView 어댑터 설정
        jobAdapterCommunity = new JobAdapter(allJobsCommunity);
        recyclerCommunity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCommunity.setAdapter(jobAdapterCommunity);

// ✅ 클릭 리스너 설정
        jobAdapterCommunity.setOnItemClickListener(position -> {
            JobModel clickedPost = allJobsCommunity.get(position);
            Log.d("MainActivity", "🔥 클릭된 아이템: " + clickedPost.getTitle() + ", ID: " + clickedPost.getId());

            // ✅ Intent 실행
            Intent intent = new Intent(MainActivity.this, ExperienceView.class);
            intent.putExtra("postId", clickedPost.getId());

            // ✅ 사용자 ID도 함께 넘김
            SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
            long userId = prefs.getLong("userId", -1);
            intent.putExtra("userId", userId);

            startActivity(intent);
            Log.d("MainActivity", "✅ Intent 실행 완료");
        });



        // ✅ "더보기" 버튼 클릭 이벤트 (현재 기능 없음)
        btnMoreSpecial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResumePremiumActivity.class);
            startActivity(intent);
        });
        btnMoreRecent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResumeNewJobActivity.class);
            startActivity(intent);
        });

        btnMoreCommunity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExperienceList.class);
            startActivity(intent);
        });

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

        apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(CommunityAPI.class);

        recyclerCommunity = findViewById(R.id.recycler_community);







        fetchCommunityPosts();
        fetchRecruitmentPosts(1 );
        fetchPremiumRecruitmentPosts();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                if (isUserLoggedIn()) {
                    UserRepository userRepository = new UserRepository(MainActivity.this);

                    userRepository.isUserCeo(isCeo -> {
                        Intent intent;
                        if (isCeo) {
                            intent = new Intent(MainActivity.this, CeoMypageActivity.class);
                        } else {
                            intent = new Intent(MainActivity.this, UserMypageActivity.class);
                        }
                        startActivity(intent);
//                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                            Intent intent;
//                            if (isCeo) {
//                                intent = new Intent(MainActivity.this, CeoMypageActivity.class);
//                            } else {
//                                intent = new Intent(MainActivity.this, UserMypageActivity.class);
//                            }
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.slide_in_left, 0);
//                        }, 2000); // 2초 지연 후 실행
                    });
                } else {
                    // 로그인 안 되어 있으면 로그인 화면으로 이동
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
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

        int size = 5;
        int page = 0;

        CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);
        Call<PostListResponse> call = apiService.getAllPosts(size, page);  // ✅ PostListResponse 사용

        call.enqueue(new Callback<PostListResponse>() {
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostListResponse postListResponse = response.body();
                    Log.d("fetchCommunityPosts", "🔥 API 응답: " + new Gson().toJson(postListResponse));

                    allJobsCommunity.clear();

                    if (postListResponse.getData() != null && postListResponse.getData().getPostList() != null) {
                        List<CommunityModel> postList = postListResponse.getData().getPostList();

                        for (CommunityModel post : postList) {
                            long postId = post.getPostId();
                            fetchPostImage(post, postId);  // ✅ 개별 게시글 API 호출 (PostDetailResponse 사용)
                        }
                    } else {
                        Log.e("fetchCommunityPosts", "❌ postList가 비어 있음.");
                        Toast.makeText(MainActivity.this, "커뮤니티 게시글 없음", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("fetchCommunityPosts", "❌ 서버 응답 실패: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                Log.e("fetchCommunityPosts", "❌ API 요청 실패: " + t.getMessage());
            }
        });
    }



    private void fetchPostImage(CommunityModel post, long postId) {
        CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);
        Call<ResponseWrapper<CommunityModel>> call = apiService.getPostById(postId);

        call.enqueue(new Callback<ResponseWrapper<CommunityModel>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<CommunityModel>> call, Response<ResponseWrapper<CommunityModel>> response) {
                Log.d("fetchPostImage", "Raw Response: " + response.raw());

                if (response.isSuccessful() && response.body() != null) {
                    CommunityModel detailedPost = response.body().getData();
                    Log.d("fetchPostImage", "🔥 상세 게시글 응답: " + new Gson().toJson(detailedPost));

                    // ✅ 이미지 URL 설정 (없으면 기본 이미지)
                    String imageUrl = (detailedPost.getFile_name() == null || detailedPost.getFile_name().isEmpty())
                            ? "android.resource://" + getPackageName() + "/" + R.drawable.b_logo
                            : detailedPost.getFile_name();

                    // ✅ 게시글을 리스트에 추가 (ID 포함)
                    allJobsCommunity.add(new JobModel(
                            detailedPost.getPostId(),
                            detailedPost.getTitle(),
                            detailedPost.getUserName(),
                            imageUrl,
                            true  // 🔥 커뮤니티 게시글임을 표시
                    ));

                    // ✅ RecyclerView 갱신
                    jobAdapterCommunity.notifyDataSetChanged();
                } else {
                    Log.e("fetchPostImage", "❌ 게시글 이미지 가져오기 실패: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<CommunityModel>> call, Throwable t) {
                Log.e("fetchPostImage", "❌ API 요청 실패: " + t.getMessage());
            }
        });
    }







    private void fetchRecruitmentPosts(int page) {
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);
        Call<RecruitmentResponse> call = recruitmentAPI.getRecruitmentPosts(1, 10);

        call.enqueue(new Callback<RecruitmentResponse>() {
            @Override
            public void onResponse(Call<RecruitmentResponse> call, Response<RecruitmentResponse> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    RecruitmentResponse recruitmentResponse = response.body();
                    Log.d("API_RESPONSE", "Message: " + recruitmentResponse.getMessage());

                    allJobsRecent.clear();

                    if (recruitmentResponse.getData() != null && recruitmentResponse.getData().getRecruitmentList() != null) {
                        List<RecruitmentModel> jobList = recruitmentResponse.getData().getRecruitmentList();

                        int maxItems = Math.min(jobList.size(), 5);
                        for (int i = 0; i < maxItems; i++) {
                            RecruitmentModel job = jobList.get(i);

                            // 🔥 이미지 URL 설정 (없으면 기본 이미지)
                            String imageUrl = (job.getFile() == null || job.getFile().isEmpty())
                                    ? "android.resource://" + getPackageName() + "/" + R.drawable.b_logo
                                    : job.getFile();

                            allJobsRecent.add(new JobModel(
                                    job.getId(),
                                    job.getTitle(),
                                    (job.getWage() != null) ? "급여: " + job.getWage() + "원" : "급여 정보 없음",
                                    imageUrl
                            ));
                        }
                    } else {
                        Log.e("API_ERROR", "recruitmentList가 비어 있음.");
                        Toast.makeText(MainActivity.this, "채용 공고 데이터 없음", Toast.LENGTH_SHORT).show();
                    }

                    jobAdapterRecent.notifyDataSetChanged();
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
        RecruitmentAPI recruitmentAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(RecruitmentAPI.class);
        Call<ResponseBody> call = recruitmentAPI.getAllRecruitmentPosts(); // ✅ 새로운 API 호출

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    RecruitmentResponse responseModel = null;
                    SharedPreferences prefs = null;
                    try {
                        prefs = EncryptedSharedPreferences.create(
                                MainActivity.this,
                                "secure_prefs",
                                new MasterKey.Builder(MainActivity.this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        );
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String fixedKey = prefs.getString("aes_key", null);
                    Log.d("AES_DEBUG", "복호화 키 길이: " + fixedKey.length());
                    Log.d("AES_DEBUG", "복호화 키 값: " + fixedKey);


                    try{
                        // 1. 응답 문자열 파싱
                        String responseBodyString = response.body().string();
//                        Log.d("AES_DEBUG", "🔐 암호화된 평문 JSON: " + responseBodyString);

                        // 2. data 필드 추출 (Base64 문자열)
                        JsonObject root = JsonParser.parseString(responseBodyString).getAsJsonObject();
                        String base64EncryptedData = root.get("data").getAsString();

                        // 3. Base64 디코딩
                        byte[] encryptedBytes = android.util.Base64.decode(base64EncryptedData, android.util.Base64.DEFAULT);
//                        Log.d("AES_DEBUG", "🔐 디코딩된 byte 길이: " + encryptedBytes.length);

                        // 4. AES 복호화
                        aesUtil aesUtil = new aesUtil(fixedKey);
                        String decryptedJson = aesUtil.decrypt(encryptedBytes);
//                        Log.d("AES_DEBUG", "✅ 복호화된 평문 JSON: " + decryptedJson);

                        // 5. JSON → 객체 변환
                        responseModel = new Gson().fromJson(decryptedJson, RecruitmentResponse.class);


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }



                    RecruitmentResponse recruitmentResponse = responseModel;
                    Log.d("API_RESPONSE", "Message: " + recruitmentResponse.getMessage());

                    allJobsSpecial.clear(); // 기존 데이터 초기화

                    if (recruitmentResponse.getData() != null && recruitmentResponse.getData().getRecruitmentList() != null) {
                        List<RecruitmentModel> jobList = recruitmentResponse.getData().getRecruitmentList();

                        int maxItems = 5;
                        int count = 0;

                        for (RecruitmentModel job : jobList) {
                            if ("Y".equalsIgnoreCase(job.getItem())) { // ✅ item = "Y" 체크
                                Log.d("API_RESPONSE", "✅ Premium Job Found: " + job.getTitle());

                                // 🔥 이미지 URL 설정 (없으면 기본 이미지)
                                String imageUrl = (job.getFile() == null || job.getFile().isEmpty())
                                        ? "android.resource://" + getPackageName() + "/" + R.drawable.b_logo
                                        : job.getFile();

                                allJobsSpecial.add(new JobModel(
                                        job.getId(),
                                        job.getTitle(),
                                        (job.getWage() != null) ? "급여: " + job.getWage() + "원" : "급여 정보 없음",
                                        imageUrl
                                ));

                                count++;
                                if (count >= maxItems) break; // 🔥 최대 5개까지만 추가
                            }
                        }

                        Log.d("API_RESPONSE", "Final Premium Job Count: " + allJobsSpecial.size());
                        jobAdapterSpecial.notifyDataSetChanged(); // ✅ RecyclerView 업데이트
                    } else {
                        Log.e("API_ERROR", "프리미엄 공고 없음.");
                        Toast.makeText(MainActivity.this, "프리미엄 공고 데이터 없음", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Response Failed. Body: " + response.errorBody());
                    Toast.makeText(MainActivity.this, "프리미엄 공고 데이터 로딩 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "프리미엄 공고 API 요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }


}