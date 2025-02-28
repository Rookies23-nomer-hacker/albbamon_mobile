package com.example.albbamon.dto.request;

public class ProfileImageRequestDto {
    private String resume_img_data;

    public ProfileImageRequestDto(String resume_img_data) {
        this.resume_img_data = resume_img_data;
    }

    public String getResume_img_data() {
        return resume_img_data;
    }

    public void setResume_img_data(String resume_img_data) {
        this.resume_img_data = resume_img_data;
    }
}
