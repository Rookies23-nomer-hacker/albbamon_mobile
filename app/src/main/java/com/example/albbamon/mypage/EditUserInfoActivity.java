package com.example.albbamon.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.albbamon.R;
import com.example.albbamon.utils.ViewPagerAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class EditUserInfoActivity extends AppCompatActivity {
    private ViewPager2 viewPager; // 🔹 클래스 멤버 변수로 변경
    private List<Fragment> fragments; // 🔹 클래스 멤버 변수로 변경

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        // 툴바 제목 및 뒤로가기 버튼
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("회원정보 수정");

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        // 2개의 탭
        TabLayout tabLayout = findViewById(R.id.multitabLayout);
        viewPager = findViewById(R.id.viewPager2);
        viewPager.setOffscreenPageLimit(2); // 🔹 프래그먼트 유지

        // 🔹 프래그먼트 리스트 초기화
        fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        fragments.add(new EditUserInfoFragment());
        titles.add("회원정보 수정");

        fragments.add(new ChangePasswordFragment());
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
            viewPager.setCurrentItem(1);
        } else {
            viewPager.setCurrentItem(0);
        }

        // 🔹 버튼 클릭 이벤트 추가
        Button customButton = findViewById(R.id.btn_change_pw);
        customButton.setOnClickListener(v -> handleCustomButtonClick());
//
//        View buttonLayout = findViewById(R.id.btn_change_pw);
//        if (buttonLayout instanceof LinearLayout) {
//            MaterialButton confirmButton = buttonLayout.findViewById(R.id.btn_confirm); // ✅ 내부 버튼 찾기
//            confirmButton.setOnClickListener(v -> handleCustomButtonClick());
//        } else {
//            Log.e("EditUserInfoActivity", "btn_change_pw is not a LinearLayout! It is: " + buttonLayout.getClass().getSimpleName());
//        }
    }

    private void handleCustomButtonClick() {
        int currentTabIndex = viewPager.getCurrentItem();

        if (currentTabIndex == 1) {  // ✅ "비밀번호 변경" 탭이 활성화된 경우
            ChangePasswordFragment fragment = (ChangePasswordFragment) getSupportFragmentManager().findFragmentByTag("f1");
            if (fragment != null) {
                fragment.handleChangePassword();
            } else {
                Toast.makeText(this, "비밀번호 변경 화면을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
