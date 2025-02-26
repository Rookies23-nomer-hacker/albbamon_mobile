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
    private LocalDateTime create_date;
    private LocalDateTime last_modified_date;

    public ResumeRequestDto(Long user_id, String school, String status, String personal,
                            String work_place_region, String work_place_city, String industry_occupation,
                            String employmentType, String working_period, String working_day,
                            String introduction, String portfolioData, String portfoliourl,
                            LocalDateTime create_date, LocalDateTime last_modified_date) {
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
        this.create_date = create_date;
        this.last_modified_date = last_modified_date;
    }

    // Getter 추가
    public Long getUser_id() { return user_id; }
    public String getSchool() { return school; }
    public String getStatus() { return status; }
    public String getPersonal() { return personal; }
    public String getWork_place_region() { return work_place_region; }
    public String getWork_place_city() { return work_place_city; }
    public String getIndustry_occupation() { return industry_occupation; }
    public String getEmploymentType() { return employmentType; }
    public String getWorking_period() { return working_period; }
    public String getWorking_day() { return working_day; }
    public String getIntroduction() { return introduction; }
    public String getPortfolioData() { return portfolioData; }
    public String getPortfoliourl() { return portfoliourl; }
    public String getPortfolioName() { return portfolioName; }
    public LocalDateTime getCreate_date() { return create_date; }
    public LocalDateTime getLast_modified_date() { return last_modified_date; }
}
