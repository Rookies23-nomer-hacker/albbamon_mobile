package com.example.albbamon;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

public class MailSettingsActivity extends AppCompatActivity {

    private Switch switchBenefitMail;
    private Switch switchAdMail;
    private Switch switchSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_settings);

        switchBenefitMail = findViewById(R.id.switchBenefitMail);
        switchAdMail = findViewById(R.id.switchAdMail);
        switchSMS = findViewById(R.id.switchSMS);

        // 예시: 스위치 상태 변경 시 Toast 메시지 표시
        switchBenefitMail.setOnCheckedChangeListener((buttonView, isChecked) ->
                Toast.makeText(this, "혜택·소식 메일: " + isChecked, Toast.LENGTH_SHORT).show()
        );

        switchAdMail.setOnCheckedChangeListener((buttonView, isChecked) ->
                Toast.makeText(this, "광고메일: " + isChecked, Toast.LENGTH_SHORT).show()
        );

        switchSMS.setOnCheckedChangeListener((buttonView, isChecked) ->
                Toast.makeText(this, "문자알림: " + isChecked, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "뒤로가기 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}

