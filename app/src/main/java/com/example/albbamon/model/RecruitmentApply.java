package com.example.albbamon.model;

import java.time.LocalDateTime;

public class RecruitmentApply {
    private Long applyId;
    private Long resumeId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String school;
    private String status;
    private String personal;
    private String workPlaceRegion;
    private String workPlaceCity;
    private String industryOccupation;
    private String employmentType;
    private String workingPeriod;
    private String workingDay;
    private String introduction;
    private String portfolioUrl;
    private String portfolioName;
    private String resumeImgUrl;
    private String resumeImgName;
    private String createDate;
    private String applyStatus;

    // 생성자
    public RecruitmentApply(Long applyId, Long resumeId, String userName, String school, String status, String personal,
                            String workPlaceRegion, String workPlaceCity, String industryOccupation,
                            String employmentType, String workingPeriod, String workingDay,
                            String introduction, String portfolioUrl, String portfolioName,
                            String resumeImgUrl, String resumeImgName, String createDate,
                            String applyStatus) {
        this.applyId = applyId;
        this.resumeId = resumeId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.school = school;
        this.status = status;
        this.personal = personal;
        this.workPlaceRegion = workPlaceRegion;
        this.workPlaceCity = workPlaceCity;
        this.industryOccupation = industryOccupation;
        this.employmentType = employmentType;
        this.workingPeriod = workingPeriod;
        this.workingDay = workingDay;
        this.introduction = introduction;
        this.portfolioUrl = portfolioUrl;
        this.portfolioName = portfolioName;
        this.resumeImgUrl = resumeImgUrl;
        this.resumeImgName = resumeImgName;
        this.createDate = createDate;
        this.applyStatus = applyStatus;
    }

    // Getter and Setter methods
    public Long getApplyId() { return applyId; }
    public Long getresumeId() { return resumeId; }
    public String getUserName() { return userName; }
    public String getUserEmail() {return userEmail; }

    public String getUserPhone() {return userPhone; }
    public String getSchool() { return school; }
    public String getStatus() { return status; }
    public String getPersonal() { return personal; }
    public String getWorkPlaceRegion() { return workPlaceRegion; }
    public String getWorkPlaceCity() { return workPlaceCity; }
    public String getIndustryOccupation() { return industryOccupation; }
    public String getEmploymentType() { return employmentType; }
    public String getWorkingPeriod() { return workingPeriod; }
    public String getWorkingDay() { return workingDay; }
    public String getIntroduction() { return introduction; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public String getPortfolioName() { return portfolioName; }
    public String getResumeImgUrl() { return resumeImgUrl; }
    public String getResumeImgName() { return resumeImgName; }
    public String getCreateDate() { return createDate; }
    public String getApplyStatus() { return applyStatus; }
}
