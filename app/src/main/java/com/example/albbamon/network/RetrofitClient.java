package com.example.albbamon.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static Retrofit retrofitWithoutSession = null; // 로그인 요청용 (세션 X)
    private static Retrofit retrofitWithSession = null;    // 인증이 필요한 요청용 (세션 O)
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:60085/")
//                    .baseUrl("http://58.127.241.84:60085") // 여기에 API 서버 주소 입력
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 변환 설정
                    .build();
        }
        return retrofit;
    }

    // ✅ 로그인 요청을 위한 Retrofit (세션 없이 요청)
    public static Retrofit getRetrofitInstanceWithoutSession() {
        if (retrofitWithoutSession == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

            retrofitWithoutSession = new Retrofit.Builder()
//                    .baseUrl("http://58.127.241.84:60085") // API 서버 주소
                    .baseUrl("http://10.0.2.2:60085/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 변환 설정
                    .build();
        }
        return retrofitWithoutSession;
    }

    // ✅ 세션을 포함하는 Retrofit (인증이 필요한 API 요청에 사용)
    public static Retrofit getRetrofitInstanceWithSession(Context context) {
        if (retrofitWithSession == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // ✅ SharedPreferences에서 `JSESSIONID` 가져오기
                            SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                            String jsessionId = prefs.getString("cookie", ""); // `cookie` 키에 저장됨

                            Log.d(TAG, "저장된 JSESSIONID: " + jsessionId);

                            // ✅ 요청 헤더에 `JSESSIONID` 추가 (세션 유지)
                            Request.Builder requestBuilder = original.newBuilder();
                            if (!jsessionId.isEmpty()) {
                                requestBuilder.header("Cookie", "JSESSIONID=" + jsessionId);
                                Log.d("SESSION", "요청 헤더에 JSESSIONID 추가: " + jsessionId);
                            } else {
                                Log.d(TAG, "JSESSIONID 없음. 인증되지 않은 요청");
                            }

                            Request request = requestBuilder
                                    .method(original.method(), original.body())
                                    .build();

                            Response response = chain.proceed(request);

                            // ✅ 응답에서 `Set-Cookie` 확인 (로그인 성공 시 쿠키를 받아올 가능성 있음)
                            String responseCookies = response.header("Set-Cookie");
                            if (responseCookies != null && responseCookies.contains("JSESSIONID")) {
                                String newSessionId = responseCookies.split(";")[0].split("=")[1]; // `JSESSIONID` 값 추출
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("cookie", newSessionId); // ✅ 새로운 `JSESSIONID` 저장
                                editor.apply();
                                Log.d(TAG, "SharedPreferences에 새로운 JSESSIONID 저장됨: " + newSessionId);
                            }

                            return response;
                        }
                    })
                    .build();

            retrofitWithSession = new Retrofit.Builder()
//                    .baseUrl("http://58.127.241.84:60085")
                    .baseUrl("http://10.0.2.2:60085/") // API 서버 주소
                    .client(okHttpClient) // ✅ JSESSIONID 포함
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 변환 설정
                    .build();
        }
        return retrofitWithSession;
    }
}
