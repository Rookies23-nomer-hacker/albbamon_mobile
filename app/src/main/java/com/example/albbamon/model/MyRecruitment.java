package com.example.albbamon.model;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyRecruitment {
    private Long id;
    private String title;
    private String createDate;
    private String dueDate;
    private String company;

    public MyRecruitment(Long id, String title, String createDate, String dueDate, String company) {
        this.id = id;
        this.title = title;
        this.createDate = createDate;
        this.dueDate = dueDate;
        this.company = company;

        Log.d("MyRecruitment", "🔥 MyRecruitment 객체 생성됨 -> recruitmentId: " + id);
    }

    public Long getRecruitmentId() {
        Log.d("MyRecruitment", "🔍 getRecruitmentId() 호출됨: " + id);
        return id; }

    public String getTitle() { return title; }
    public String getCreateDate() { return createDate; }
    public String getCompany() { return company; }

    // ✅ 날짜 변환 메서드 추가
    private String formatDate(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return "날짜 없음";
        }
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, inputFormatter);
            return dateTime.format(outputFormatter);
        } catch (Exception e) {
            return "날짜 오류";
        }
    }

    // ✅ 변환된 날짜 반환
    public String getFormattedCreateDate() {
        return formatDate(createDate);
    }

    public String getFormattedDueDate() {
        return formatDate(dueDate);
    }

    // 모집 상태 반환 (현재 날짜와 비교)
    public String getExpiredStatus() {
        if (dueDate == null || dueDate.isEmpty()) {
            return "날짜 정보 없음";
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dueDateParsed = LocalDateTime.parse(dueDate, formatter);
            return dueDateParsed.toLocalDate().isBefore(LocalDateTime.now().toLocalDate()) ? "|  열람 기간 만료" : "|  모집 중";
        } catch (Exception e) {
            return "날짜 변환 오류"; // ❌ 형식이 잘못되었을 경우 기본 메시지 반환
        }
    }

}
