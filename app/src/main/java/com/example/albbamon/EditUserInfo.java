package com.example.albbamon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.albbamon.model.UserInfo;
import com.example.albbamon.repository.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserInfo extends Fragment {


    public EditUserInfo() {
        // 기본 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_info, container, false);

        EditText userEmail = view.findViewById(R.id.et_email); // 여기서 findViewById 사용

        // UserRepository 초기화
        UserRepository userRepository;
        userRepository = new UserRepository();

        // ✅ fetchUserInfo() 호출하여 사용자 정보 가져오기
        userRepository.fetchUserInfo(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                // ✅ 사용자 정보 출력
                userEmail.setText(userInfo.getEmail() != null ? userInfo.getEmail() : "이메일 없음");

            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("UserMypage", errorMessage);
            }
        });

        return view;
    }
}
