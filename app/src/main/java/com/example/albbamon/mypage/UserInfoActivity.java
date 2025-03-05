package com.example.albbamon.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.MemberWithdrawalActivity;
import com.example.albbamon.R;
import com.example.albbamon.sign.SignInActivity;
import com.example.albbamon.api.UserAPI;
import com.example.albbamon.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
            Log.d("UserInfoActivity", "Starting EditUserInfoActivity with fragment: change_password");

            startActivity(intent);
        });

        ChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
            intent.putExtra("fragment", "change_password"); // ChangePassword Fragment 표시
            Log.d("UserInfoActivity", "Starting EditUserInfoActivity with fragment: change_password");
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

        UserAPI apiService = RetrofitClient.getRetrofitInstanceWithSession(this).create(UserAPI.class);
        Call<Void> call = apiService.signOut();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Logout", "로그아웃 성공");

                    SharedPreferences prefs = getSharedPreferences("SESSION", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("userId"); // userId 삭제
                    editor.remove("cookie"); // 세션 쿠키 삭제
                    editor.clear();
                    editor.commit();

                    // 자동 로그인 정보 삭제
                    SharedPreferences eCache = getSharedPreferences("ECACHE", MODE_PRIVATE);
                    SharedPreferences.Editor cacheEditor = eCache.edit();
                    cacheEditor.clear(); // 캐시데이터 전체 삭제
                    cacheEditor.commit();
                    Log.d("Logout", "SESSION & ECACHE 삭제됨: " + prefs.getAll() + ", " + eCache.getAll());

                    // 로그아웃 후 SignIn 이동
                    Intent intent = new Intent(UserInfoActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 모든 이전 액티비티 제거
                    startActivity(intent);
                    finish();

                } else {
                    Log.e("Logout", "로그아웃 실패! 응답 코드: " + response.code());
                    Toast.makeText(UserInfoActivity.this, "로그아웃 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Logout", "API 호출 실패: " + t.getMessage());
                Toast.makeText(UserInfoActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });




    }


}
