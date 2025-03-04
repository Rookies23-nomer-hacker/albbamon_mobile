package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class ApplyStatusModel {
    private Long applyId;
    private String recruitmentTitle;
    private Integer recruitmentWage;
    private String company;
    private String createDate;  // LocalDateTime을 String으로 받기 (ISO 8601 형식)
    private String status;

    // Getter and Setter
    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getRecruitmentTitle() {
        return recruitmentTitle;
    }

    public void setRecruitmentTitle(String recruitmentTitle) {
        this.recruitmentTitle = recruitmentTitle;
    }

    public Integer getRecruitmentWage() {
        return recruitmentWage;
    }

    public void setRecruitmentWage(Integer recruitmentWage) {
        this.recruitmentWage = recruitmentWage;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ApplyStatusModel{" +
                "applyId=" + applyId +
                ", title='" + recruitmentTitle + '\'' +
                ", companyName='" + recruitmentWage + '\'' +
                ", status='" + company + '\'' +
                '}';
    }

}