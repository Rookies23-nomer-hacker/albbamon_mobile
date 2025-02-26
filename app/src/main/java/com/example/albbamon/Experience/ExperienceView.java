package com.example.albbamon.Experience;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.albbamon.R;
import com.example.albbamon.adapter.CommunityAdapter;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.api.ResponseWrapper;
import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.network.RetrofitClient;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class ExperienceView extends AppCompatActivity {
    TextView title_text; TextView content_text; TextView date_text; TextView name_text;

    TextView ad_title_1; TextView ad_title_2;  TextView ad_desc_1;TextView ad_desc_2;
    ImageView img_view; ImageView ad_image_1; ImageView ad_image_2;
    Integer postId; ImageView back_img_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_view);
        //세션 처리 추가
        // Intent에서 데이터 가져오기


        // 로그 출력 (디버깅용)
        Log.d("DetailActivity", "Post ID: " + postId);

        // 화면에 표시하는 로직 추가 가능
        title_text = (TextView) findViewById(R.id.title_text);
        content_text = (TextView) findViewById(R.id.content_text);
        date_text = (TextView) findViewById(R.id.date_text);
        ad_title_1 = (TextView) findViewById(R.id.tv_ad_title_1);
        ad_title_2 = (TextView) findViewById(R.id.tv_ad_title_2);
        ad_desc_1 = (TextView) findViewById(R.id.tv_ad_desc_1);
        ad_desc_2 = (TextView) findViewById(R.id.tv_ad_desc_2);
        ad_image_1 = (ImageView) findViewById(R.id.iv_ad_image1);
        ad_image_2 = (ImageView) findViewById(R.id.iv_ad_image2);
        img_view = (ImageView) findViewById(R.id.img_view);
        name_text = (TextView) findViewById(R.id.name_text);
        back_img_btn = (ImageView) findViewById(R.id.back_img_btn);

        postId = getIntent().getIntExtra("postId", -1);
        fetchCommunity();

        back_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 현재 액티비티 종료 (이전 액티비티로 이동)
            }
        });
    }

    private void fetchCommunity() {
        CommunityAPI apiService = RetrofitClient.getRetrofitInstance().create(CommunityAPI.class);

        Call<ResponseWrapper<CommunityModel>> call = apiService.getPostById(postId); // API 호출

        call.enqueue(new Callback<ResponseWrapper<CommunityModel>>() { // ResponseWrapper 사용!
            @Override
            public void onResponse(Call<ResponseWrapper<CommunityModel>> call, Response<ResponseWrapper<CommunityModel>> response) {
                Log.d("API_RESPONSE", "Raw Response: " + response.raw());

                if (response.isSuccessful() && response.body() != null) {
                    ResponseWrapper<CommunityModel> responseWrapper = response.body(); // 전체 응답
                    CommunityModel bbs = responseWrapper.getData(); // `data` 필드에서 실제 데이터 가져오기

                    if (bbs != null) {
                        String date = bbs.getCreateDate().substring(0, 10) + " " +
                                bbs.getCreateDate().substring(11, 16);

                        title_text.setText(bbs.getTitle());
                        name_text.setText(bbs.getUserName());
                        content_text.setText(bbs.getContents());
                        date_text.setText(date);

                        Log.d("API_SUCCESS", "게시글 로드 완료: " + bbs.getTitle());
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
}