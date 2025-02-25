package com.example.albbamon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.albbamon.model.UserInfo;
import com.example.albbamon.repository.UserRepository;

public class ChangePassword extends Fragment {
    private UserRepository userRepository;
    private EditText userEmail, nowPwInput, newPwInput, newPwReInput;
    private Button changePwButton;
    private Long userId = 1L; // TODO: 실제 로그인된 유저 ID로 변경해야 함

    public ChangePassword() {
        // 기본 생성자
    }

    public static ChangePassword newInstance() {
        return new ChangePassword();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        // ✅ UI 요소 초기화
        userEmail = view.findViewById(R.id.et_email);
        nowPwInput = view.findViewById(R.id.now_pw);
        newPwInput = view.findViewById(R.id.new_pw);
        newPwReInput = view.findViewById(R.id.new_pw_re);
        changePwButton = view.findViewById(R.id.btn_change_pw);

        // UserRepository 초기화
        userRepository = new UserRepository(requireContext());

        // ✅ fetchUserInfo() 호출하여 사용자 정보 가져오기
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                // ✅ 사용자 정보 출력
                userEmail.setText(userInfo.getEmail() != null ? userInfo.getEmail() : "이메일 없음");
                userId = userInfo.getId(); // 유저 ID 저장
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ChangePassword", errorMessage);
            }
        });

        // ✅ 버튼 클릭 시 비밀번호 변경 API 호출
        changePwButton.setOnClickListener(v -> handleChangePassword());

        return view;
    }

    // ✅ 비밀번호 변경 요청 함수
    private void handleChangePassword() {
        String nowPw = nowPwInput.getText().toString().trim();
        String newPw = newPwInput.getText().toString().trim();
        String newPwRe = newPwReInput.getText().toString().trim();

        // 입력값 검증
        if (nowPw.isEmpty() || newPw.isEmpty() || newPwRe.isEmpty()) {
            Toast.makeText(getContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPw.equals(newPwRe)) {
            Toast.makeText(getContext(), "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ 서버에 비밀번호 변경 요청
        userRepository.changePassword(userId, nowPw, newPw, new UserRepository.PasswordCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                clearInputFields(); // 입력 필드 초기화
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("ChangePassword", errorMessage);
            }
        });
    }

    // ✅ 입력 필드 초기화
    private void clearInputFields() {
        nowPwInput.setText("");
        newPwInput.setText("");
        newPwReInput.setText("");
    }
}
