package com.example.albbamon.Experience;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;
import com.example.albbamon.adapter.CommunityAdapter;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.model.CommunityModel;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_list);

        list_view = (ListView) findViewById(R.id.ex_list);
        efab_write_btn = (ExtendedFloatingActionButton)findViewById(R.id.ex_write_btn);
        fab_up_btn = (FloatingActionButton) findViewById(R.id.ex_scroll_top);
        fab_write_btn = (FloatingActionButton) findViewById(R.id.ex_scroll_write);
        total_bbs = (TextView) findViewById(R.id.totalRec_textView);



        //ListView 데이터 가져와서 보여주기
        fetchCommunity();


        list_view.setOnItemClickListener((parent, view, position, id) -> {
            // 클릭한 아이템 가져오기
            CommunityModel selectedPost = communityList.get(position);

            // 데이터 확인 (디버깅용)
            Log.d("ListViewClick", "선택된 아이템: " + selectedPost.getTitle());

            // 새 액티비티로 이동 (선택한 게시글 정보를 넘겨줌)
            Intent intent = new Intent(ExperienceList.this, ExperienceView.class);
            intent.putExtra("postId", selectedPost.getPostId()); // 게시글 ID 전달
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

        setClickListeners();
    }

    private void setClickListeners() {
        fab_up_btn.setOnClickListener(v -> onScrollTopClick(v));
        efab_write_btn.setOnClickListener(v -> onWriteClick(v));
        fab_write_btn.setOnClickListener(v -> onWriteClick(v));
    }

    private void onWriteClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ExperienceCreate.class);
        startActivity(intent);
    }

    public void onScrollTopClick(View view) {
        list_view.smoothScrollToPosition(0); // 리스트뷰 맨 위로 이동
    }

    private void fetchCommunity() {
        CommunityAPI apiService = RetrofitClient.getRetrofitInstance().create(CommunityAPI.class);

        Call<List<CommunityModel>> call = apiService.getPosts();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CommunityModel>> call, Response<List<CommunityModel>> response) {
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
}