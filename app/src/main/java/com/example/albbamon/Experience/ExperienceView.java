package com.example.albbamon.Experience;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.example.albbamon.R;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.api.ResponseWrapper;
import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.network.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienceView extends AppCompatActivity {
    private TextView title_text, content_text, date_text, name_text;
    private ImageView img_view, back_img_btn;
    private ImageButton menu_img_btn;
    private long postId;
    private boolean isMyPost = false;
    private long bbs_userId;
    private long userId; // SharedPreferences에서 가져올 사용자 ID
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_view);

        postId = getIntent().getLongExtra("postId", -1);
        userId = getIntent().getLongExtra("userId", -1);

        Log.d("DetailActivity", "Post ID: " + postId);

        title_text = findViewById(R.id.title_text);
        content_text = findViewById(R.id.content_text);
        date_text = findViewById(R.id.date_text);
        name_text = findViewById(R.id.name_text);
        img_view = findViewById(R.id.img_view);
        back_img_btn = findViewById(R.id.back_img_btn);
        menu_img_btn = findViewById(R.id.menu_img_btn);

        // SharedPreferences에서 사용자 ID 가져오기
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        userId = prefs.getLong("userId", -1); // 기본값 -1 (저장된 값이 없을 경우)
        Log.d("Session", "User ID (로그인 사용자): " + userId);

        // API 호출 (게시글 정보 가져오기)
        fetchPostData();

        // 뒤로 가기 버튼
        back_img_btn.setOnClickListener(view -> finish());

        // 메뉴 버튼 클릭 시 `BottomSheetDialog` 실행
        if (menu_img_btn != null) {
            menu_img_btn.setOnClickListener(view -> showBottomSheetDialog());
        } else {
            Log.e("ExperienceView", "menu_img_btn을 찾을 수 없음!");
        }
    }

    // 게시글 데이터 불러오기 (비동기 API)
    private void fetchPostData() {
        CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);

        Call<ResponseWrapper<CommunityModel>> call = apiService.getPostById(postId);
        call.enqueue(new Callback<ResponseWrapper<CommunityModel>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<CommunityModel>> call, Response<ResponseWrapper<CommunityModel>> response) {
                Log.d("API_RESPONSE", "Raw Response: " + response.raw());

                if (response.isSuccessful() && response.body() != null) {
                    CommunityModel bbs = response.body().getData();
                    if (bbs != null) {
                        String date = bbs.getCreateDate().substring(0, 10) + " " + bbs.getCreateDate().substring(11, 16);

                        title_text.setText(bbs.getTitle());
                        name_text.setText(bbs.getUserName());
                        content_text.setText(bbs.getContents());
                        date_text.setText(date);

                        bbs_userId = bbs.getUserId();
                        imageUrl = bbs.getFile_name();
                        loadServerImage(imageUrl);

                        Log.d("API_SUCCESS", "게시글 작성자 ID: " + bbs_userId);

                        // API 응답을 받은 후 isMyPost 설정
                        isMyPost = (userId == bbs_userId);
                        Log.d("Session", "isMyPost: " + isMyPost);
                    } else {
                        Log.e("API_ERROR", "data 필드가 null 입니다.");
                        date_text.setText("날짜 정보 없음");
                    }
                } else {
                    Log.e("API_ERROR", "서버 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<CommunityModel>> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    //이미지 view 함수
    private void loadServerImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // 캐싱 전략 사용 (속도 향상)
//                    .placeholder(R.drawable.) // 로딩 중 표시할 이미지
//                    .error(R.drawable.) // 에러 발생 시 표시할 이미지
                    .into(img_view); // ImageView에 적용
            img_view.setVisibility(View.VISIBLE);
        }
    }

    // ✅ BottomSheetDialog 표시
    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.experience_bottom_sheet_menu, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        LinearLayout menuReport = bottomSheetView.findViewById(R.id.menu_report);
        LinearLayout menuBlock = bottomSheetView.findViewById(R.id.menu_block);
        LinearLayout menuEdit = bottomSheetView.findViewById(R.id.menu_edit);
        LinearLayout menuDelete = bottomSheetView.findViewById(R.id.menu_delete);

        // ✅ isMyPost를 API 응답 후 결정
        if (isMyPost) {
            menuEdit.setVisibility(View.VISIBLE);
            menuDelete.setVisibility(View.VISIBLE);
            menuReport.setVisibility(View.GONE);
            menuBlock.setVisibility(View.GONE);
        } else {
            menuEdit.setVisibility(View.GONE);
            menuDelete.setVisibility(View.GONE);
            menuReport.setVisibility(View.VISIBLE);
            menuBlock.setVisibility(View.VISIBLE);
        }

        // 신고하기 버튼 클릭 이벤트
        menuReport.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Toast.makeText(this, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // 차단하기 버튼 클릭 이벤트
        menuBlock.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            Toast.makeText(this, "차단이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // 수정하기 버튼 클릭 이벤트
        menuEdit.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            postEdit();
        });

        // 삭제하기 버튼 클릭 이벤트
        menuDelete.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            postDelete();
        });

        bottomSheetDialog.show();
    }

    private void postEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정하기")
                .setMessage("이 게시글을 수정하시겠습니까?")
                .setPositiveButton("예", (dialog, which) -> {
                    Intent intent = new Intent(ExperienceView.this, ExperienceUpdate.class);
                    intent.putExtra("postId", postId); // 게시글 ID
                    startActivity(intent);
                })
                .setNegativeButton("아니요", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void postDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제하기")
                .setMessage("정말로 이 게시글을 삭제하시겠습니까?")
                .setPositiveButton("예", (dialog, which) -> {
                    //삭제 로직
                    CommunityAPI apiService = RetrofitClient.getRetrofitInstanceWithoutSession().create(CommunityAPI.class);
                    Map<String, Object> requestBody = new HashMap<>();
                    Log.d("요청한 값", "요청한 postid, userid: " + postId+" "+userId);
                    Call<Void> call = apiService.mobiledeletePost(postId, userId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ExperienceView.this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ExperienceView.this, ExperienceList.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ExperienceView.this, "삭제 실패", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ExperienceView.this, "삭제 요청 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("아니요", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
