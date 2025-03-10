package com.example.albbamon.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor; // 이미 import 되어 있음
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static Retrofit retrofitWithoutSession = null;
    private static Retrofit retrofitWithSession = null;
    private static String sessionCookie = null; // 세션 쿠키 저장 변수
    private static final String BASE_URL = "http://www.albbamon.com:44380/";
//    private static final String BASE_URL = "http://58.127.241.84:60085/";
//    private static final String BASE_URL = "http://192.168.0.6:60085/";
//    private static final String BASE_URL = "http://10.0.2.2:60085/";

    // ✅ 로그인 요청을 위한 Retrofit (세션 없이 요청)
    public static Retrofit getRetrofitInstanceWithoutSession() {
        if (retrofitWithoutSession == null) {

            // 추가됨: HttpLoggingInterceptor 추가 (logging level BODY)
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // 추가됨
                    .build();

            retrofitWithoutSession = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofitWithoutSession;
    }

    // ✅ 세션을 포함하는 Retrofit (세션 유지)
    public static Retrofit getRetrofitInstanceWithSession(Context context) {
        if (retrofitWithSession == null) {
            // 추가됨: HttpLoggingInterceptor 추가 (logging level BODY)
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // 추가됨
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            for (Cookie cookie : cookies) {
                                if (cookie.name().equals("JSESSIONID")) {
                                    sessionCookie = cookie.toString(); // 전체 쿠키 저장
                                    SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("cookie", sessionCookie); // SharedPreferences에 저장
                                    editor.apply();
                                    Log.d(TAG, "세션 저장: " + sessionCookie);
                                }
                            }
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            if (sessionCookie != null) {
                                return Collections.singletonList(Cookie.parse(url, sessionCookie));
                            }
                            return Collections.emptyList();
                        }
                    })
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // ✅ SharedPreferences에서 세션 쿠키 가져오기
                            SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                            String savedCookie = prefs.getString("cookie", "");

                            Request.Builder requestBuilder = original.newBuilder();
                            if (!savedCookie.isEmpty()) {
                                requestBuilder.header("Cookie", savedCookie);
                                Log.d(TAG, "요청 헤더에 쿠키 추가: " + savedCookie);
                            }

                            Request request = requestBuilder.method(original.method(), original.body()).build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofitWithSession = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitWithSession;
    }

    public static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    // 기존 RetrofitClient 클래스 아래에 추가합니다.
    public static Retrofit getApplyRetrofitInstance() {
        // BASE_URL 뒤에 "api/apply/"를 추가한 URL로 Retrofit 인스턴스를 생성합니다.
        return new Retrofit.Builder()
                .baseUrl(BASE_URL + "api/apply/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
