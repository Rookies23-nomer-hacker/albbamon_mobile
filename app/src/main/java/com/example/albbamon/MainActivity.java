package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.Resume.ResumeWriteActivity;
import com.example.albbamon.mypage.UserMypageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버튼 초기화
        Button btnPer = findViewById(R.id.goAccount);
        Button btnOpenMypage = findViewById(R.id.btn_open_mypage);
        Button btnOpenen = findViewById(R.id.btn_open_a);

        btnPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });

        // 버튼 클릭 시 UserMypage 이동
        btnOpenMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserMypageActivity.class);
                startActivity(intent);
            }
        });


        btnOpenen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResumeWriteActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_open_inquiry).setOnClickListener( v -> {
            Intent intent = new Intent(this, InquiryActivity.class);
            startActivity(intent);
        });

    }
}