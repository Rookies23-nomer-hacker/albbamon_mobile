package com.example.albbamon.dto.request;

import java.time.LocalDateTime;

public class ResumeRequestDto {
    private Long user_id;
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
    private String portfolioData;
    private String portfoliourl;
    private String portfolioName;
    private String resume_imgurl;
    private String resume_img_name;
    private String resume_img_data;

    public ResumeRequestDto(Long user_id, String school, String status, String personal,
                            String work_place_region, String work_place_city, String industry_occupation,
                            String employmentType, String working_period, String working_day,
                            String introduction, String portfolioData, String portfoliourl, String portfolioName,
                            String resume_imgurl, String resume_img_name, String resume_img_data
                            ) {
        this.user_id = user_id;
        this.school = school;
        this.status = status;
        this.personal = personal;
        this.work_place_region = work_place_region;
        this.work_place_city = work_place_city;
        this.industry_occupation = industry_occupation;
        this.employmentType = employmentType;
        this.working_period = working_period;
        this.working_day = working_day;
        this.introduction = introduction;
        this.portfolioData = portfolioData;
        this.portfoliourl = portfoliourl;
        this.portfolioName = portfolioName;
        this.resume_imgurl = resume_imgurl;
        this.resume_img_name = resume_img_name;
        this.resume_img_data = resume_img_data;
    }

}
