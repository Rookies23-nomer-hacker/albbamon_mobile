package com.example.albbamon.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.albbamon.R;
import com.example.albbamon.utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class EditUserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        // 툴바 제목 및 뒤로가기 버튼
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("회원정보 수정");

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(EditUserInfoActivity.this, UserInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 2개의 탭
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // 🔹 탭 목록과 프래그먼트 동적 추가 가능
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        fragments.add(new EditUserInfo());
        titles.add("회원정보 수정");

        fragments.add(new ChangePassword());
        titles.add("비밀번호 변경");

        // 🔹 어댑터 설정
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, fragments, titles);
        viewPager.setAdapter(adapter);

        // 🔹 TabLayout + ViewPager2 연결
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(adapter.getTabTitle(position));
        }).attach();

        // 🔹 Intent로 전달된 데이터 확인 후 해당 탭으로 이동
        String fragmentType = getIntent().getStringExtra("fragment");
        if ("change_password".equals(fragmentType)) {
            viewPager.setCurrentItem(1); // 비밀번호 변경 탭으로 이동
        } else {
            viewPager.setCurrentItem(0); // 기본값: 회원정보 수정 탭
        }
    }
}
