package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    ArrayList<String> items = new ArrayList<String>();
    ExtendedFloatingActionButton efab_write_btn;
    FloatingActionButton fab_up_btn;
    FloatingActionButton fab_write_btn;
    boolean isExtraVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_list);

        list_view = (ListView) findViewById(R.id.ex_list);
        efab_write_btn = (ExtendedFloatingActionButton)findViewById(R.id.ex_write_btn);
        fab_up_btn = (FloatingActionButton) findViewById(R.id.ex_scroll_top);
        fab_write_btn = (FloatingActionButton) findViewById(R.id.ex_scroll_write);


//        for(int i=0; i < 100; i++){
//            items.add("Item : " + Integer.toString(i));
//        }



        fetchCommunity();












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

        call.enqueue(new Callback<List<CommunityModel>>() {
            @Override
            public void onResponse(Call<List<CommunityModel>> call, Response<List<CommunityModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CommunityModel> bbss = response.body();

                    runOnUiThread(() -> { // UI 업데이트가 있다면 runOnUiThread() 사용
                        for (CommunityModel bbs : bbss) {
//                            Integer postId = (bbs.getTitle() != null) ? bbs.getPostId() : 0;
                            String userId = (bbs.getUserId() != null) ? bbs.getUserId() : "알 수 없음";
                            String title = (bbs.getTitle() != null) ? bbs.getTitle() : "제목 없음";
                            String createDate = (bbs.getTitle() != null) ? bbs.getCreateDate() : "알 수 없음";

//                            items.add("번호 : " + postId.toString());
//                            items.add("제목 : " + title);
//                            items.add("작성자 : " + userId);
//                            items.add("작성일 : " + createDate);
                            Log.d("API", "ID: " + userId + ", 제목: " + title);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExperienceList.this, android.R.layout.simple_list_item_1,items) ;
                        list_view.setAdapter(adapter);
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