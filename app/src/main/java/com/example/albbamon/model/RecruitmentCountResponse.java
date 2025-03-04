package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecruitmentCountResponse {
    private int status;
    private String message;

    @SerializedName("data") // ✅ "data"를 객체로 받도록 설정
    private RecruitmentData data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public RecruitmentData getData() { return data; }

    public static class RecruitmentData {
        private int count; // ✅ "count" 필드 추가
        public int getCount() { return count; }
    }
}
