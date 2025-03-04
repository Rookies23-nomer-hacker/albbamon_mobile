package com.example.albbamon.network;

import com.example.albbamon.model.ApplyCountResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SupportStatusService {

    // 기존 다른 API 메서드들이 있다면 그대로 두고,
    // 나의 지원서 개수를 반환하는 엔드포인트를 추가합니다.
    @GET("api/mobile/apply/count")
    Call<ApplyCountResponse> getMyApplyCount();
}
