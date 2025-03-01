package com.example.albbamon.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyRecruitment {
    private Long recruitmentId;
    private String title;
    private String createDate;
    private String dueDate;
    private String company;

    public MyRecruitment(Long recruitmentId, String title, String createDate, String dueDate, String company) {
        this.recruitmentId = recruitmentId;
        this.title = title;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.company = company;
    }

    public Long getRecruitmentId() { return recruitmentId; }
    public String getTitle() { return title; }
    public String getCreateDate() { return createDate; }
    public String getDueDate() { return dueDate; }
    public String getCompany() { return company; }

    // 모집 상태 반환 (현재 날짜와 비교)
    public String getExpiredStatus() {
        if (dueDate == null || dueDate.isEmpty()) {
            return "날짜 정보 없음";
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dueDateParsed = LocalDateTime.parse(dueDate, formatter);
            return dueDateParsed.toLocalDate().isBefore(LocalDateTime.now().toLocalDate()) ? "열람 기간 만료" : "모집 중";
        } catch (Exception e) {
            return "날짜 변환 오류"; // ❌ 형식이 잘못되었을 경우 기본 메시지 반환
        }
    }

}
