package com.example.albbamon.dto.response;

public class ProfileImageResponseDto {
    private String message;
    private String resume_img_name;
    private String resume_img_url;
    private String error;

    public String getMessage() {
        return message;
    }

    public String getResume_img_name() {
        return resume_img_name;
    }

    public String getResume_img_url() {
        return resume_img_url;
    }

    public String getError() {
        return error;
    }
}
