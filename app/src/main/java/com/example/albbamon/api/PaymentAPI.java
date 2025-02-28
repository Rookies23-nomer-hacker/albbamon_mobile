package com.example.albbamon.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PaymentAPI {
    @GET("/api/payment/findUserId")  // ✅ 결제된 유저 ID 리스트 가져오기
    Call<List<Long>> getPremiumUserIds();
}
