package com.example.albbamon.dto.response;

import com.google.gson.annotations.SerializedName;

public class UserChangePwResponseDto {
    @SerializedName("message")
    private String message;

    public UserChangePwResponseDto() {} // 기본 생성자

    public UserChangePwResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}

