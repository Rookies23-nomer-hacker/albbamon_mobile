package com.example.albbamon.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albbamon.R;
import com.example.albbamon.api.ResponseWrapper;
import com.example.albbamon.model.ApplyStatusModel;
import com.example.albbamon.model.ApplyStatusModel;
import com.example.albbamon.network.RetrofitClient;
import com.example.albbamon.api.UserAPI;
import com.example.albbamon.adapter.ApplyStatusAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSupportFragment extends Fragment {
    private RecyclerView recyclerView;
    private ApplyStatusAdapter applyStatusAdapter;
    private long userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment의 레이아웃을 설정합니다.
        View rootView = inflater.inflate(R.layout.all_support_fragment, container, false);

        // RecyclerView 초기화
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // 세로 레이아웃

        // SharedPreferences에서 사용자 ID 가져오기
        userId = getActivity().getSharedPreferences("SESSION", Context.MODE_PRIVATE).getLong("userId", -1);

        if (userId != -1) {
            // Retrofit을 사용하여 API 호출
            fetchApplyList();
        } else {
            Toast.makeText(getContext(), "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private void fetchApplyList() {
        // SharedPreferences에서 userId를 가져옵니다.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SESSION", Context.MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", -1);

        if (userId != -1) {
            // Retrofit을 사용하여 API 호출
            UserAPI userAPI = RetrofitClient.getRetrofitInstanceWithSession(getContext()).create(UserAPI.class);

            // API 호출
            Call<ResponseWrapper<ResponseWrapper.ApplyData>> call = userAPI.findApplyVoByUserId(userId);
            call.enqueue(new Callback<ResponseWrapper<ResponseWrapper.ApplyData>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<ResponseWrapper.ApplyData>> call, Response<ResponseWrapper<ResponseWrapper.ApplyData>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseWrapper.ApplyData applyData = response.body().getData();
                        if (applyData != null && applyData.getApplyList() != null && !applyData.getApplyList().isEmpty()) {
                            List<ApplyStatusModel> applyVoList = applyData.getApplyList();
                            applyStatusAdapter = new ApplyStatusAdapter(applyVoList);
                            recyclerView.setAdapter(applyStatusAdapter);
                        } else {
                            Toast.makeText(getContext(), "지원서 목록이 비어 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "API 호출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<ResponseWrapper.ApplyData>> call, Throwable t) {
                    Log.e("API Error", t.getMessage(), t);
                    Toast.makeText(getContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}