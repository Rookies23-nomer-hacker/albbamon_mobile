package com.example.albbamon.Experience;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.albbamon.R;
import com.example.albbamon.adapter.CommunityAdapter;
import com.example.albbamon.api.CommunityAPI;
import com.example.albbamon.api.ResponseWrapper;
import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienceSearch extends AppCompatActivity {
    ImageButton back_img_btn;
    EditText etSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_search);

        back_img_btn = findViewById(R.id.back_img_btn);
        etSearch = findViewById(R.id.etSearch);

        back_img_btn.setOnClickListener(view -> finish());

        // 키보드에서 "검색" 또는 "Enter" 버튼을 눌렀을 때 실행
        etSearch.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                String keyword = etSearch.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    performSearch(keyword); // 검색 실행
                } else {
                    Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
            return false;
        });
    }

    // 실제 검색을 수행하는 메서드
    private void performSearch(String keyword) {
        // 🔥 여기에 검색 API 호출 또는 검색 결과 처리 추가
        Toast.makeText(this, "검색 실행: " + keyword, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ExperienceList.class);
        intent.putExtra("keyword", etSearch.getText().toString());
        startActivity(intent);
    }

}