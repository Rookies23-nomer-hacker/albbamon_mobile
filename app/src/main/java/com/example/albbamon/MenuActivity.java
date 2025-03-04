package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.Experience.ExperienceList;


import com.example.albbamon.Resume.ResumeDetailActivity;
import com.example.albbamon.Resume.ResumeNewJobActivity;
import com.example.albbamon.Resume.ResumePortfolioActivity;
import com.example.albbamon.Resume.ResumeWriteActivity;
import com.example.albbamon.api.ResumeAPI;
import com.example.albbamon.mypage.ResumeManagementActivity;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private RecyclerView menuRecyclerView;
    private CategoryAdapter categoryAdapter;
    private MenuAdapter menuAdapter;

    private List<String> categoryList;
    private List<MenuModel> menuList;
    private UserRepository userRepository;
    private ResumeAPI resumeAPI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView btnHome = findViewById(R.id.btn_home);
        ImageView btnClose = findViewById(R.id.btn_close);

        // 홈 버튼 클릭 시 홈으로 이동
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // X 버튼 클릭 시 홈으로 이동
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // 좌측 카테고리 RecyclerView 설정
        categoryRecyclerView = findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 우측 메뉴 RecyclerView 설정
        menuRecyclerView = findViewById(R.id.menu_recycler_view);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 카테고리 리스트 추가
        categoryList = new ArrayList<>();
        categoryList.add("채용정보");
        categoryList.add("회원서비스");
        categoryList.add("인재정보");
        categoryList.add("알바토크");
        categoryList.add("고객센터");

        // 카테고리 어댑터 설정
        categoryAdapter = new CategoryAdapter(categoryList, position -> updateMenuList(position));
        categoryRecyclerView.setAdapter(categoryAdapter);

        // 기본 메뉴 리스트 설정 (채용정보 선택 시 보이는 메뉴)
        updateMenuList(0);
        categoryAdapter.setSelectedPosition(0);
    }

    // 카테고리 선택 시 메뉴 리스트 업데이트
    private void updateMenuList(int categoryIndex) {
        menuList = new ArrayList<>();

        switch (categoryIndex) {
            case 0: // 채용정보
                menuList.add(new MenuModel("스크랩알바", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("최근본알바", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("맞춤알바", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("AI 추천알바", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("전체알바", R.drawable.sample_job));
                menuList.add(new MenuModel("급구알바", R.drawable.ico_favorite_off));
                break;
            case 1: // 회원서비스
                menuList.add(new MenuModel("이력서관리", R.drawable.sample_job));
                menuList.add(new MenuModel("이력서작성", R.drawable.sample_job));
                menuList.add(new MenuModel("지원현황", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("포트폴리오관리", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("공고관리", R.drawable.sample_job));
                menuList.add(new MenuModel("공고등록", R.drawable.sample_job));
                break;
            case 2: // 인재정보
                menuList.add(new MenuModel("인재정보 홈", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("전체인재", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("내 주변 인재", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("실시간 활동 인재", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("대학생 인재", R.drawable.ico_favorite_off));
                break;
            case 3: // 알바토크
                menuList.add(new MenuModel("알바경험담", R.drawable.sample_job));
                menuList.add(new MenuModel("한줄톡", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("면접족보", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("알바후기", R.drawable.ico_favorite_off));
                break;
            case 4: // 고객센터
                menuList.add(new MenuModel("공지사항", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("문의하기", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("자주 묻는 질문", R.drawable.ico_favorite_off));
                menuList.add(new MenuModel("이용가이드", R.drawable.ico_favorite_off));
                break;
        }

        // 메뉴 리스트 업데이트
        menuAdapter = new MenuAdapter(menuList, position -> {
            // 🔹 메뉴 클릭 시 해당 액티비티로 이동하도록 처리
            String selectedMenu = menuList.get(position).getTitle();
            Intent intent = null;

            switch (selectedMenu) {
                case "전체알바":
                    intent = new Intent(MenuActivity.this, ResumeNewJobActivity.class);
                    break;
                case "이력서관리":
                    intent = new Intent(MenuActivity.this, ResumeManagementActivity.class);
                    break;
                case "이력서작성":
                    checkResumeAndNavigate();
                    return;
                case "공고등록":
                    intent = new Intent(MenuActivity.this, JobPostingActivity.class);
                    break;
                case "알바경험담":
                    intent = new Intent(MenuActivity.this, ExperienceList.class);
                    break;
                // 여기에 다른 메뉴도 추가 가능
            }

            if (intent != null) {
                startActivity(intent);
            }
        });
        menuRecyclerView.setAdapter(menuAdapter);
    }

    /**
     * ✅ 이력서 존재 여부 확인 후 액티비티 이동 함수
     */
    private void checkResumeAndNavigate() {
        if (userRepository == null) {
            userRepository = new UserRepository(this);
        }
        long userId = userRepository.getUserId();

        if (resumeAPI == null) {
            resumeAPI = RetrofitClient.getRetrofitInstanceWithSession(this).create(ResumeAPI.class);
        }

        resumeAPI.checkResumeExists(userId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean exists = response.body(); // 서버 응답값 (true or false)
                    Log.d("MenuActivity", "이력서 존재 여부: " + exists);

                    if (exists) {
                        // ✅ 이력서가 존재하면 이동 차단 및 메시지 표시
                        Toast.makeText(MenuActivity.this, "이력서가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MenuActivity.this, ResumeDetailActivity.class);
                        startActivity(intent);
                    } else {
                        // ✅ 이력서가 없으면 작성 액티비티로 이동
                        Intent intent = new Intent(MenuActivity.this, ResumeWriteActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Log.e("MenuActivity", "이력서 존재 여부 체크 실패");
                    Toast.makeText(MenuActivity.this, "이력서 확인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("MenuActivity", "API 호출 실패: " + t.getMessage());
                Toast.makeText(MenuActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
