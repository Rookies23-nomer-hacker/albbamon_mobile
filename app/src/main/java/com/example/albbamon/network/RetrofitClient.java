package com.example.albbamon.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static Retrofit retrofitWithoutSession = null;
    private static Retrofit retrofitWithSession = null;
    private static final String BASE_URL = "https://www.albbamon.com:44380/";

    // ✅ 로그인 요청을 위한 Retrofit (세션 없이 요청)
    public static Retrofit getRetrofitInstanceWithoutSession() {
        if (retrofitWithoutSession == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
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
                    .addInterceptor(loggingInterceptor)
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            StringBuilder cookieHeader = new StringBuilder();

                            for (Cookie cookie : cookies) {
                                Log.d(TAG, "🍪 응답 쿠키 확인: " + cookie.toString());

                                // 모든 쿠키 저장
                                cookieHeader.append(cookie.name()).append("=").append(cookie.value()).append("; ");

                                if (cookie.name().equals("JSESSIONID")) {
                                    editor.putString("jsessionid", cookie.value());
                                    Log.d(TAG, "🔐 JSESSIONID 저장됨: " + cookie.value());
                                }
                                if (cookie.name().equals("AWSALB")) {
                                    editor.putString("AWSALB", cookie.value());
                                }
                                if (cookie.name().equals("AWSALBCORS")) {
                                    editor.putString("AWSALBCORS", cookie.value());
                                }
                            }

                            editor.putString("cookie", cookieHeader.toString().trim());
                            editor.apply();
                            Log.d(TAG, "🔒 저장된 전체 쿠키: " + cookieHeader.toString().trim());
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                            String savedCookies = prefs.getString("cookie", "");
                            String savedJsessionId = prefs.getString("jsessionid", "");
                            String savedAwsAlb = prefs.getString("AWSALB", "");
                            String savedAwsAlbCors = prefs.getString("AWSALBCORS", "");

                            List<Cookie> cookieList = new ArrayList<>();

                            if (!savedCookies.isEmpty()) {
                                String[] cookiesArray = savedCookies.split("; ");
                                for (String cookieStr : cookiesArray) {
                                    String[] parts = cookieStr.split("=", 2);
                                    if (parts.length == 2) {
                                        Cookie cookie = new Cookie.Builder()
                                                .domain(url.host())
                                                .path("/")
                                                .name(parts[0])
                                                .value(parts[1])
                                                .httpOnly()
                                                .secure()
                                                .build();
                                        cookieList.add(cookie);
                                    }
                                }
                            }

                            if (!savedAwsAlb.isEmpty()) {
                                cookieList.add(new Cookie.Builder()
                                        .domain(url.host())
                                        .path("/")
                                        .name("AWSALB")
                                        .value(savedAwsAlb)
                                        .httpOnly()
                                        .secure()
                                        .build());
                            }
                            if (!savedAwsAlbCors.isEmpty()) {
                                cookieList.add(new Cookie.Builder()
                                        .domain(url.host())
                                        .path("/")
                                        .name("AWSALBCORS")
                                        .value(savedAwsAlbCors)
                                        .httpOnly()
                                        .secure()
                                        .build());
                            }

                            if (!savedJsessionId.isEmpty()) {
                                cookieList.add(new Cookie.Builder()
                                        .domain(url.host())
                                        .path("/")
                                        .name("JSESSIONID")
                                        .value(savedJsessionId)
                                        .httpOnly()
                                        .secure()
                                        .build());
                                Log.d(TAG, "📤 JSESSIONID 추가됨: " + savedJsessionId);
                            }

                            return cookieList;
                        }
                    })
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            SharedPreferences prefs = context.getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                            String savedCookies = prefs.getString("cookie", "");
                            String savedJsessionId = prefs.getString("jsessionid", "");
                            String savedAwsAlb = prefs.getString("AWSALB", "");
                            String savedAwsAlbCors = prefs.getString("AWSALBCORS", "");

                            Request.Builder requestBuilder = original.newBuilder();
                            StringBuilder cookieHeader = new StringBuilder(savedCookies);

                            if (!savedAwsAlb.isEmpty()) {
                                cookieHeader.append("AWSALB=").append(savedAwsAlb).append("; ");
                            }
                            if (!savedAwsAlbCors.isEmpty()) {
                                cookieHeader.append("AWSALBCORS=").append(savedAwsAlbCors).append("; ");
                            }
                            if (!savedJsessionId.isEmpty()) {
                                cookieHeader.append("JSESSIONID=").append(savedJsessionId).append("; ");
                            }

                            requestBuilder.header("Cookie", cookieHeader.toString().trim());
                            Log.d(TAG, "📨 최종 요청 헤더: " + cookieHeader.toString().trim());

                            return chain.proceed(requestBuilder.build());
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

    public static Retrofit getApplyRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL + "api/apply/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}