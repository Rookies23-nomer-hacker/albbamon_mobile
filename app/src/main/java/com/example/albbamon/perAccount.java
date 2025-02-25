package com.example.albbamon;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.albbamon.api.SignUpAPI;
import com.example.albbamon.model.SignUpModel;
import com.example.albbamon.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class perAccount extends AppCompatActivity {

    private EditText emailInput, passwordInput, phoneInput, nameInput, ceoNumInput, companyInput;
    private CheckBox allAgreeCheckbox, agree1, agree2, agree3;
    private Button registerButton;
    // 입력 이력 확인용 변수 (이메일: hasInput[0], 비밀번호: hasInput[1])
    private boolean[] hasInput = new boolean[2]; // 0: 이메일, 1: 비밀번호

    boolean isCorrect = false; // 오입력 방지 변수

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

        // EditText 초기화
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        phoneInput = findViewById(R.id.phoneInput);
        nameInput = findViewById(R.id.nameInput);


        // CheckBox 초기화
        allAgreeCheckbox = findViewById(R.id.allAgreeCheckbox);
        agree1 = findViewById(R.id.agree1);
        agree2 = findViewById(R.id.agree2);
        agree3 = findViewById(R.id.agree3);

        // 가입 버튼 초기화
        registerButton = findViewById(R.id.registerButton);
        registerButton.setEnabled(false);

        // 가입 버튼 클릭 이벤트 -> API 통신
        registerButton.setOnClickListener(v -> registerUser());

        // TextWatcher 설정
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    if (emailInput.hasFocus()) {
                        hasInput[0] = true; // 이메일 필드 입력 시작
                    } else if (passwordInput.hasFocus()) {
                        hasInput[1] = true; // 비밀번호 필드 입력 시작
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                boolean emailValid = emailValid(email);
                boolean passwordValid = passwordValid(password);

                // ✅ 이메일 유효성 검사 (한 번이라도 입력된 경우에만 적용)
                if (hasInput[0]) {
                    if (!emailValid) {
                        incorrectEditView(emailInput, Color.RED);
                    } else {
                        incorrectEditView(emailInput, Color.parseColor("#D6D7D7"));
                    }
                }

                // ✅ 비밀번호 유효성 검사 (한 번이라도 입력된 경우에만 적용)
                if (hasInput[1]) {
                    if (!passwordValid) {
                        incorrectEditView(passwordInput, Color.RED);
                    } else {
                        incorrectEditView(passwordInput, Color.parseColor("#D6D7D7"));
                    }
                }

                // ✅ 이메일과 비밀번호가 모두 유효하면 isCorrect = true
                isCorrect = emailValid && passwordValid;


                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };





        emailInput.addTextChangedListener(textWatcher);
        passwordInput.addTextChangedListener(textWatcher);
        phoneInput.addTextChangedListener(textWatcher);
        nameInput.addTextChangedListener(textWatcher);

        // 체크박스 이벤트 설정
        View.OnClickListener checkBoxListener = v -> checkInputs();
        agree1.setOnClickListener(checkBoxListener);
        agree2.setOnClickListener(checkBoxListener);
        agree3.setOnClickListener(checkBoxListener);

        // "전체 동의" 체크 시 나머지 체크박스 자동 체크
        allAgreeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            agree1.setChecked(isChecked);
            agree2.setChecked(isChecked);
            agree3.setChecked(isChecked);
            checkInputs();
        });

        // 개별 체크박스 해제 시 "전체 동의" 체크 해제
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

    private void incorrectEditView(EditText editText, int color){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(2, color);
        drawable.setCornerRadius(7);
        editText.setBackground(drawable);
    }

    // 이메일 유효성 검사 (영어 1개 이상 + 숫자 1개 이상 + 5자 이상)
    private boolean emailValid(String email){
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");    }

    // 패스워드 유효성 검사 (영어 소문자 1개 이상 + 숫자 1개 이상 + 8자 이상)
    private boolean passwordValid(String password) {
        return password.matches("^(?=.*[a-z])(?=.*\\d).{8,}$");
    }

    // 회원가입 API 호출
    private void registerUser() {

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();



        SignUpAPI apiService = RetrofitClient.getRetrofitInstance().create(SignUpAPI.class);
        Log.d("API_REQUEST", "email: " + email + ", passwd: " + password + ", phone: " + phone + ", name: " + name);

        Call<SignUpModel> call = apiService.createUser(new SignUpModel(email, password, phone, name));
        Log.e("API_ERROR", "서버 응답 실패: " + apiService);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(perAccount.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(perAccount.this, "회원가입 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "서버 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                Toast.makeText(perAccount.this, "네트워크 오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Error: " + t.getMessage());
                t.printStackTrace(); // 전체 오류 로그 출력
            }
        });
    }

    // ✅ 모든 입력값이 채워졌는지 확인하는 메서드
    private void checkInputs() {
        boolean isAllFilled = !emailInput.getText().toString().trim().isEmpty() &&
                !passwordInput.getText().toString().trim().isEmpty() &&
                !phoneInput.getText().toString().trim().isEmpty() &&
                !nameInput.getText().toString().trim().isEmpty();

        boolean isAllChecked = agree1.isChecked() && agree2.isChecked() && agree3.isChecked();

        if (isAllFilled && isAllChecked) {
            registerButton.setEnabled(true);
            registerButton.setBackgroundColor(Color.parseColor("#FFA500")); // 활성화 색상
        } else {
            registerButton.setEnabled(false);
            registerButton.setBackgroundColor(Color.parseColor("#A9A9A9")); // 비활성화 색상
        }
    }
}
