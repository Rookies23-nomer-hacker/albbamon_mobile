package com.example.albbamon.model;

public class ApplyItem {
    private String status;  // ✅ 지원 상태 (예: "지원완료", "면접", "합격" 등)

    // ✅ 생성자
    public ApplyItem(String status) {
        this.status = status;
    }

    // ✅ Getter 메서드
    public String getStatus() {
        return status;
    }
}
