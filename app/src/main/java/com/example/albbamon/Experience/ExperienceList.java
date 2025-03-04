package com.example.albbamon.Experience;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.MainActivity;
import com.example.albbamon.MenuActivity;
import com.example.albbamon.R;
import com.example.albbamon.adapter.CommunityAdapter;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.api.PostListResponse;
import com.example.albbamon.api.ResponseWrapper;
import com.example.albbamon.dto.request.PostData;
import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.model.PageInfo;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.albbamon.network.RetrofitClient;

import retrofit2.Callback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;



public class ExperienceList extends AppCompatActivity {
    ListView list_view;
    ExtendedFloatingActionButton efab_write_btn;
    FloatingActionButton fab_up_btn;
    FloatingActionButton fab_write_btn;
    boolean isExtraVisible = false;
    List<CommunityModel> communityList = new ArrayList<>();
    TextView total_bbs;
    ImageButton searchButton, menuButton;
    Button btnPrev, btnNext;
    LinearLayout pageNumbersContainer, paginationLayout;
    int size, page, totalPages, currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_list);

        list_view = (ListView) findViewById(R.id.ex_list);
        efab_write_btn = (ExtendedFloatingActionButton)findViewById(R.id.ex_write_btn);
        fab_up_btn = (FloatingActionButton) findViewById(R.id.ex_scroll_top);
        fab_write_btn = (FloatingActionButton) findViewById(R.id.ex_scroll_write);
        total_bbs = (TextView) findViewById(R.id.totalRec_textView);
        searchButton = findViewById(R.id.searchButton);
        menuButton = findViewById(R.id.menuButton);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        pageNumbersContainer = findViewById(R.id.pageNumbersContainer);
        paginationLayout = findViewById(R.id.paginationLayout);

        size = 10;
        page = 0;
        String keyword = getIntent().getStringExtra("keyword");
        if (keyword == null){
            //ListView 데이터 가져와서 보여주기
            postList(page);
        }else{
            postSearchList(keyword);
        }

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExperienceList.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, 0);
            }
        });

        list_view.setOnItemClickListener((parent, view, position, id) -> {
            // 클릭한 아이템 가져오기
            CommunityModel selectedPost = communityList.get(position);

            // 데이터 확인 (디버깅용)
            Log.d("ListViewClick", "선택된 아이템: "+selectedPost.getPostId()+" "+selectedPost.getTitle());

            // 새 액티비티로 이동 (선택한 게시글 정보를 넘겨줌)
            Intent intent = new Intent(ExperienceList.this, ExperienceView.class);
            intent.putExtra("postId", selectedPost.getPostId()); // 게시글 ID
            startActivity(intent);
        });

        //스크롤 액션에 따른 btn설정
        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //스크롤 중
                    fab_write_btn.hide();  // 글쓰기 버튼 숨기기
                } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //스크롤이 멈췄을 때
                    if(isExtraVisible){
                        fab_write_btn.show();
                    } else {
                        fab_write_btn.hide();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i > 2) { // 스크롤이 내려갔을 때
                    if (!isExtraVisible) {
                        efab_write_btn.hide();
                        fab_up_btn.show();

                        isExtraVisible = true;
                    }
                } else { // 다시 올라갔을 때
                    if (isExtraVisible) {
                        efab_write_btn.show();
                        fab_write_btn.hide();
                        fab_up_btn.hide();
                        isExtraVisible = false;
                    }
                }
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                postList(currentPage);
            }
        });

        // 다음 버튼 클릭 시
        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                postList(currentPage);
            }
        });

        setClickListeners();
    }

    private void setClickListeners() {
        fab_up_btn.setOnClickListener(v -> onScrollTopClick(v));
        efab_write_btn.setOnClickListener(v -> onWriteClick(v));
        fab_write_btn.setOnClickListener(v -> onWriteClick(v));
        searchButton.setOnClickListener(v -> onSearchClick(v));
    }

    private void onWriteClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ExperienceCreate.class);
        startActivity(intent);
    }
    private void onSearchClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ExperienceSearch.class);
        startActivity(intent);
    }

    public void onScrollTopClick(View view) {
        list_view.smoothScrollToPosition(0); // 리스트뷰 맨 위로 이동
    }

    private void postList(int page) {
        CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);

        Call<PostListResponse> call = apiService.getAllPosts(size, page);
        call.enqueue(new Callback<PostListResponse>() {
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostData data = response.body().getData();

                    if (data != null && data.getPostList() != null) {
                        List<CommunityModel> postList = data.getPostList();
                        PageInfo pageinfo = data.getPageInfo();
                        totalPages = pageinfo.getTotalPages();
                        currentPage = pageinfo.getPageNum();
                        Log.e("API_ERROR", " page num : "+pageinfo.getPageNum());
                        runOnUiThread(() -> {
                            communityList.clear();  // 기존 데이터 삭제 (중복 방지)
                            communityList.addAll(postList);  // 데이터를 communityList에 추가
                            CommunityAdapter adapter = new CommunityAdapter(ExperienceList.this, communityList);
                            list_view.setAdapter(adapter);
                            total_bbs.setText("총 " + communityList.size() + "건");


                        });

                        // 리스트뷰가 끝나면 페이지네이션 UI 표시
                        if (totalPages > 1) {
                            paginationLayout.setVisibility(View.VISIBLE);
                            updatePaginationButtons();
                        } else {
                            paginationLayout.setVisibility(View.GONE); // 한 페이지만 있으면 숨김
                        }
                    } else {
                        Log.e("API_ERROR", "데이터가 null 또는 postList가 비어 있음");
                    }
                } else {
                    Log.e("API_ERROR", "서버 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) { // ✅ 타입 수정
                Log.e("API_ERROR", "Error: " + t.getMessage());
                t.printStackTrace(); // 전체 오류 로그 출력
            }
        });
    }

    private void postSearchList(String keyword) {
        CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);
        Call<List<CommunityModel>> call = apiService.getSearchlist(keyword);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CommunityModel>> call, Response<List<CommunityModel>> response) {
                Log.e("keyword", "응답 코드: " + response.code()); // 서버 응답 코드 출력
                if (response.isSuccessful() && response.body() != null) {
                    List<CommunityModel> bbs = response.body();
                    runOnUiThread(() -> {
                        runOnUiThread(() -> {
                            communityList.clear();  // 기존 데이터 삭제 (중복 방지)
                            communityList.addAll(bbs);  // communityList에 데이터 추가
                        });
                        CommunityAdapter adapter = new CommunityAdapter(ExperienceList.this, communityList);
                        list_view.setAdapter(adapter);
                        total_bbs.setText("총 " + communityList.size() + "건");
                    });
                } else {
                    Log.e("API_ERROR", "서버 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CommunityModel>> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                t.printStackTrace(); // 전체 오류 로그 출력
            }
        });
    }

    private void updatePaginationButtons() {
        pageNumbersContainer.removeAllViews(); // 기존 버튼 제거

        for (int i = 0; i <= totalPages; i++) {
            Button pageButton = new Button(this);
            pageButton.setText(String.valueOf(i+1));
            pageButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // 현재 페이지 표시
            if (i == currentPage - 1) {
                pageButton.setEnabled(false); // 현재 페이지는 비활성화
            }

            // 페이지 버튼 클릭 이벤트
            final int pageNumber = i;
            pageButton.setOnClickListener(v -> {
                currentPage = pageNumber + 1;
                postList(currentPage);
            });

            pageNumbersContainer.addView(pageButton);
        }

        // 이전/다음 버튼 활성화 여부 설정
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }
}