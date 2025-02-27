package com.example.albbamon.mypage;

import android.content.Intent;
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
    private EditText nowPwInput, newPwInput, newPwReInput;
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
        nowPwInput = view.findViewById(R.id.now_pw);
        newPwInput = view.findViewById(R.id.new_pw);
        newPwReInput = view.findViewById(R.id.new_pw_re);

        nowPwInput.setFocusableInTouchMode(true);
        newPwInput.setFocusableInTouchMode(true);
        newPwReInput.setFocusableInTouchMode(true);

        // UserRepository 초기화
        userRepository = new UserRepository(requireContext());

        // fetchUserInfo() 호출하여 로그인된 사용자 정보 가져오기
//        fetchUserInfo();

        return view;
    }

    // 로그인된 사용자 정보 가져오기
//    private void fetchUserInfo() {
//        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
//            @Override
//            public void onSuccess(UserInfo userInfo) {
//                userId = userInfo.getId(); // 로그인된 유저 ID 저장
//                Log.d("ChangePassword", "로그인된 사용자 ID: " + userId);
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                Log.e("ChangePassword", errorMessage);
//                Toast.makeText(requireContext(), "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // 비밀번호 변경 요청 함수
    public void handleChangePassword() {

        String nowPw = nowPwInput.getText().toString().trim();
        String newPw = newPwInput.getText().toString().trim();
        String newPwRe = newPwReInput.getText().toString().trim();

        // 입력값 검증
        if (nowPw.isEmpty() || newPw.isEmpty() || newPwRe.isEmpty()) {
            Toast.makeText(requireContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPw.equals(newPwRe)) {
            Toast.makeText(requireContext(), "새로운 비밀번호 두 개의 값이 일치하지 않습니다. 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🚀 **이제 실제 입력된 비밀번호 값을 전달**
        userRepository.changePassword(nowPw, newPw, new UserRepository.PasswordCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d("ChangePassword", "비밀번호 변경 성공: " + message);
                Toast.makeText(requireContext(), "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                clearInputFields();

                // UserInfoActivity로 이동
                Intent intent = new Intent(requireContext(), UserInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // 기존 스택을 정리하고 이동
                startActivity(intent);

                // 현재 Activity 종료 (만약 ChangePasswordFragment가 Activity에 포함된 경우)
                requireActivity().finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ChangePassword", "비밀번호 변경 실패: " + errorMessage);
                Toast.makeText(requireContext(), "비밀번호 변경에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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
