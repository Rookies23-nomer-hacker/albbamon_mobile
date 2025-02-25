package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        LinearLayout EditUserInfoActivity = findViewById(R.id.edit_member_info);
        LinearLayout ChangePassword = findViewById(R.id.edit_password);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("회원정보");

        ImageView backButton = findViewById(R.id.back);
        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, UserMypage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        EditUserInfoActivity.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
            intent.putExtra("fragment", "edit_user_info"); // EditUserInfo Fragment 표시
            startActivity(intent);
        });

        ChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
            intent.putExtra("fragment", "change_password"); // ChangePassword Fragment 표시
            startActivity(intent);
        });
    }
}
