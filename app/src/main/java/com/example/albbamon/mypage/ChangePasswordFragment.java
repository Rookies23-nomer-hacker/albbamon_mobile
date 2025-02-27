package com.example.albbamon.mypage;

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

import com.example.albbamon.R;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.repository.UserRepository;

public class ChangePasswordFragment extends Fragment {
    private UserRepository userRepository;
    private EditText userEmail, nowPwInput, newPwInput, newPwReInput;
    private Button changePwButton;
    private Long userId = null;

    public ChangePasswordFragment() {
        // 기본 생성자
    }

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        // UI 요소 초기화
        userEmail = view.findViewById(R.id.et_email);
        nowPwInput = view.findViewById(R.id.now_pw);
        newPwInput = view.findViewById(R.id.new_pw);
        newPwReInput = view.findViewById(R.id.new_pw_re);
        changePwButton = view.findViewById(R.id.btn_change_pw);

        // UserRepository 초기화
        userRepository = new UserRepository(requireContext());

        // fetchUserInfo() 호출하여 로그인된 사용자 정보 가져오기
        fetchUserInfo();

        // 버튼 클릭 시 비밀번호 변경 API 호출
        changePwButton.setOnClickListener(v -> handleChangePassword());

        return view;
    }

    // 로그인된 사용자 정보 가져오기
    private void fetchUserInfo() {
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                userEmail.setText(userInfo.getEmail() != null ? userInfo.getEmail() : "이메일 없음");
                userId = userInfo.getId(); // 로그인된 유저 ID 저장
                Log.d("ChangePassword", "로그인된 사용자 ID: " + userId);
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ChangePassword", errorMessage);
                Toast.makeText(requireContext(), "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 비밀번호 변경 요청 함수
    private void handleChangePassword() {
        if (userId == null) {
            Toast.makeText(requireContext(), "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nowPw = nowPwInput.getText().toString().trim();
        String newPw = newPwInput.getText().toString().trim();
        String newPwRe = newPwReInput.getText().toString().trim();

        // 입력값 검증
        if (nowPw.isEmpty() || newPw.isEmpty() || newPwRe.isEmpty()) {
            Toast.makeText(requireContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPw.equals(newPwRe)) {
            Toast.makeText(requireContext(), "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 서버에 비밀번호 변경 요청
        userRepository.changePassword(requireContext(), userId, nowPw, newPw, new UserRepository.PasswordCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                clearInputFields(); // 입력 필드 초기화
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("ChangePassword", errorMessage);
            }
        });
    }

    // 입력 필드 초기화
    private void clearInputFields() {
        nowPwInput.setText("");
        newPwInput.setText("");
        newPwReInput.setText("");
    }
}
