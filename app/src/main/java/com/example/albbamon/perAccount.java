package com.example.albbamon;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class perAccount extends AppCompatActivity {

    private EditText[] editTexts;
    private CheckBox allAgreeCheckbox, agree1, agree2, agree3;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_per_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ✅ EditText 배열로 관리 (한 번에 적용)
        editTexts = new EditText[]{
                findViewById(R.id.emailInput),
                findViewById(R.id.passwordInput),
                findViewById(R.id.nameInput),
                findViewById(R.id.phoneInput),
                findViewById(R.id.birthInput),
                findViewById(R.id.addressInput)
        };

        // ✅ CheckBox 초기화
        allAgreeCheckbox = findViewById(R.id.allAgreeCheckbox);
        agree1 = findViewById(R.id.agree1);
        agree2 = findViewById(R.id.agree2);
        agree3 = findViewById(R.id.agree3);

        // ✅ 가입 버튼 초기화
        registerButton = findViewById(R.id.registerButton);

        // ✅ TextWatcher 설정 (입력 상태 감지)
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // ✅ 모든 EditText에 TextWatcher 추가
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(textWatcher);
        }

        // ✅ 개별 체크박스 변경 감지
        View.OnClickListener checkBoxListener = v -> checkInputs();
        agree1.setOnClickListener(checkBoxListener);
        agree2.setOnClickListener(checkBoxListener);
        agree3.setOnClickListener(checkBoxListener);

        // ✅ "전체 동의" 체크 시 나머지 체크박스 자동 체크
        allAgreeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            agree1.setChecked(isChecked);
            agree2.setChecked(isChecked);
            agree3.setChecked(isChecked);
            checkInputs();
        });

        // ✅ 개별 체크박스 해제 시 "전체 동의" 체크 해제
        CompoundButton.OnCheckedChangeListener individualCheckBoxListener = (buttonView, isChecked) -> {
            if (!isChecked) {
                allAgreeCheckbox.setChecked(false);
            }
            checkInputs();
        };

        agree1.setOnCheckedChangeListener(individualCheckBoxListener);
        agree2.setOnCheckedChangeListener(individualCheckBoxListener);
        agree3.setOnCheckedChangeListener(individualCheckBoxListener);
    }

    // ✅ 모든 입력값이 채워졌는지 확인하는 메서드
    private void checkInputs() {
        boolean isAllFilled = true;

        // 모든 EditText가 비어있는지 확인
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                isAllFilled = false;
                break;
            }
        }

        // 모든 필수 CheckBox가 체크되었는지 확인
        if (!agree1.isChecked() || !agree2.isChecked() || !agree3.isChecked()) {
            isAllFilled = false;
        }

        // 모든 조건을 충족하면 버튼 활성화 (주황색)
        if (isAllFilled) {
            registerButton.setEnabled(true);
            registerButton.setBackgroundColor(Color.parseColor("#FFA500")); // 주황색
        } else {
            registerButton.setEnabled(false);
            registerButton.setBackgroundColor(Color.parseColor("#A9A9A9")); // 회색 (비활성화)
        }
    }
}
