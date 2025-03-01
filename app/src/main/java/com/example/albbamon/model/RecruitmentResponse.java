package com.example.albbamon.model;

import java.util.List;

public class RecruitmentResponse {
    private int status;
    private String message;
    private RecruitmentData data; // ✅ "data" 필드를 객체로 매핑

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public RecruitmentData getData() {
        return data;
    }

    // 🔥 "data" 내부에 있는 recruitmentList를 감싸는 클래스 추가
    public static class RecruitmentData {
        private List<RecruitmentModel> recruitmentList;

        public List<RecruitmentModel> getRecruitmentList() {
            return recruitmentList;
        }
    }
}
