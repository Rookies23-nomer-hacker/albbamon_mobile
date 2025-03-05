package com.example.albbamon.Resume;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albbamon.R;

public class ResumeIntroActivity extends AppCompatActivity {

    private TextView tvCharCount;
    private EditText editIntro, edit_content;
    private ResumeDataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume_intro);

        editIntro = findViewById(R.id.etIntro);
        dataManager = ResumeDataManager.getInstance();
        tvCharCount = findViewById(R.id.tvCharCount);

        findViewById(R.id.BackIcon).setOnClickListener(v -> finish());

        // ✅ 이전에 저장한 자기소개 불러오기
        String savedIntro = dataManager.getIntroduction();
        if (savedIntro != null) {
            editIntro.setText(savedIntro);
        }

        // 글자 수 업데이트
        editIntro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvCharCount.setText(charSequence.length() + " / 50,000");
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String introText = editIntro.getText().toString();

            // ✅ 자기소개를 ResumeDataManager에 저장
            dataManager.setIntroduction(introText);

            // ✅ 저장 완료 메시지
            Toast.makeText(this, "자기소개 저장완료", Toast.LENGTH_SHORT).show();

            // ✅ ResumeWriteActivity로 이동 + 결과 값 전달 (10글자 + "...")
            Intent resultIntent = new Intent();
            resultIntent.putExtra("introContent", getTrimmedIntro(introText));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    // ✅ 자기소개 10글자 + "..."으로 변환하는 함수
    private String getTrimmedIntro(String introText) {
        if (introText.length() > 10) {
            return introText.substring(0, 10) + "...";
        } else {
            return introText;
        }
    }
}
