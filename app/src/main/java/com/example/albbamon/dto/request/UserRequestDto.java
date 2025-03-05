package com.example.albbamon.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserRequestDto {
    @SerializedName("userId")
    private long userId;  // ✅ long 타입 유지

    public UserRequestDto(long userId) {
        this.userId = userId;  // ✅ 변환 없이 그대로 저장
    }

    public long getUserId() {
        return userId;
    }
}
