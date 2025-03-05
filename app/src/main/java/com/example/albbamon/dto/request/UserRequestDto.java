package com.example.albbamon.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserRequestDto {
    @SerializedName("userId")
    private long userId;  // ✅ long 타입 유지

    public UserRequestDto(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
