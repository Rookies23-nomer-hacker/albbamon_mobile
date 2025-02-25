package com.example.albbamon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // 시스템 바 패딩 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

// 개인 회원가입 버튼 클릭 시 inputPerAccount 화면으로 이동
        Button btnPer = findViewById(R.id.buttonPer);
        btnPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), perAccount.class);
                intent.putExtra("isCompany", false);
                startActivity(intent);
            }
        });

// 기업 회원가입 버튼 클릭 시 inputComAccount 화면으로 이동
        Button btnCom = findViewById(R.id.buttonCom);
        btnCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), comAccount.class);
                intent.putExtra("isCompany", true);
                startActivity(intent);
            }
        });


    }
}
