package com.example.albbamon.dto.response;

public class ResumeResponseDto {
    private long userId;
    private String school;
    private String status;
    private String personal;
    private String work_place_region;
    private String work_place_city;
    private String industry_occupation;
    private String employmentType;
    private String working_period;
    private String working_day;
    private String introduction;
    private String portfolioName;
    private String portfoliourl;
    private String resume_imgurl;  // ✅ 추가
    private String resume_img_name;  // ✅ 추가
    private String resume_img_data;  // ✅ 추가

    public Long getUserId() {
        return userId;
    }
    public String getSchool() {
        return school;
    }

    public String getStatus() {
        return status;
    }

    public String getPersonal() {
        return personal;
    }

    public String getWork_place_region() {
        return work_place_region;
    }

    public String getWork_place_city() {
        return work_place_city;
    }

    public String getIndustry_occupation() {
        return industry_occupation;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getWorking_period() {
        return working_period;
    }

    public String getWorking_day() {
        return working_day;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public String getPortfoliourl() {
        return portfoliourl;
    }
    public String getResume_imgurl() { return resume_imgurl; }
    public String getResume_img_name() { return resume_img_name; }
    public String getResume_img_data() { return resume_img_data; }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

}
