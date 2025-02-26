package com.example.albbamon.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://58.127.241.84:60085") // 여기에 API 서버 주소 입력
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 변환 설정
                    .build();
        }
        return retrofit;
    }
}