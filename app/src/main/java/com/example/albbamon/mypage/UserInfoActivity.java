package com.example.albbamon.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.MainActivity;
import com.example.albbamon.MemberWithdrawalActivity;
import com.example.albbamon.R;

public class UserInfoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        LinearLayout EditUserInfoActivity = findViewById(R.id.edit_member_info);
        LinearLayout ChangePassword = findViewById(R.id.edit_password);
        LinearLayout MemberWithdrawalActivity = findViewById(R.id.Withdraw);
        Button btnLogout = findViewById(R.id.btn_logout);


        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("회원정보");

        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, UserMypageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        EditUserInfoActivity.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, com.example.albbamon.mypage.EditUserInfoActivity.class);
            intent.putExtra("fragment", "edit_user_info"); // EditUserInfo Fragment 표시
            startActivity(intent);
        });

        ChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
            intent.putExtra("fragment", "change_password"); // ChangePassword Fragment 표시
            startActivity(intent);
        });


        MemberWithdrawalActivity.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, MemberWithdrawalActivity.class);
            intent.putExtra("fragment", "change_password"); // ChangePassword Fragment 표시
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("userId"); // userId 삭제
        editor.remove("cookie"); // 세션 쿠키 삭제
        editor.apply();

        // ✅ 로그아웃 후 MainActivity로 이동
        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 모든 이전 액티비티 제거
        startActivity(intent);
        finish();
    }


}
