package com.example.albbamon.dto.request;

public class UserRequestDto {
    private long userId;

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
