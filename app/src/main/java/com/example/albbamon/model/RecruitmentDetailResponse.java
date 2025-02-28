package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class RecruitmentDetailResponse { // ✅ 단일 공고 조회를 위한 Response 클래스
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data") // ✅ 단일 공고 데이터가 "data" 안에 있음
    private RecruitmentModel data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public RecruitmentModel getData() { // ✅ 단일 공고 데이터 반환
        return data;
    }
}
