package com.example.albbamon.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.albbamon.R;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.repository.UserRepository;

public class EditUserInfo extends Fragment {

    public EditUserInfo() {
        // 기본 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_info, container, false);

        EditText userEmail = view.findViewById(R.id.et_email);

        // UserRepository 초기화
        UserRepository userRepository = new UserRepository(requireContext());

        // fetchUserInfo() 호출하여 사용자 정보 가져오기 (Context를 첫 번째 인자로 전달)
        userRepository.fetchUserInfo(requireContext(), new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                userEmail.setText(userInfo.getEmail() != null ? userInfo.getEmail() : "이메일 없음");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("EditUserInfo", errorMessage);
            }
        });

        return view;
    }
}
