package com.example.albbamon.dto.request;

import com.google.gson.annotations.SerializedName;

public class UpdateApplyStatusRequestDto {
    @SerializedName("status")
    private String status;

    public UpdateApplyStatusRequestDto(String status) {
        // ✅ status가 null이면 기본값을 "WAITING"으로 설정
        this.status = (status != null) ? status.toUpperCase() : "WAITING";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // ✅ status가 null이 아니면 대문자로 변환
        this.status = (status != null) ? status.toUpperCase() : "WAITING";
    }
}
