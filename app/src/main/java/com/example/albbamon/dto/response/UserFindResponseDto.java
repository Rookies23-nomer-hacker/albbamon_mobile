package com.example.albbamon.dto.response;

import com.google.gson.annotations.SerializedName;

public class UserFindResponseDto {
    @SerializedName("email")
    private String email;

    @SerializedName("type")
    private String type;

    @SerializedName("success")
    private boolean success;

    // Builder 패턴을 사용한 생성자 예시
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String type;
        private boolean success;

        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder type(String type) {
            this.type = type;
            return this;
        }
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }
        public UserFindResponseDto build() {
            UserFindResponseDto dto = new UserFindResponseDto();
            dto.email = this.email;
            dto.type = this.type;
            dto.success = this.success;
            return dto;
        }
    }

    // Getter 메서드
    public String getEmail() {
        return email;
    }
    public String getType() {
        return type;
    }
    public boolean isSuccess() {
        return success;
    }
}
