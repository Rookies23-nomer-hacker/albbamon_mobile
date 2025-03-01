package com.example.albbamon.model;


public class ResumeModel {
    private long resumeId;
    private long userId;
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
    private String career;
    private String address;
    private String workType;

    public ResumeModel(String createDate, String career, String address, String workType) {
        this.createDate = createDate;
        this.career = career;
        this.address = address;
        this.workType = workType;
    }

    public long getResumeId() {
        return resumeId;
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setResumeId(long resumeId) {
        this.resumeId = resumeId;
    }
    public String getCareer() {
        return career;
    }

    public long getUserId() {
        return userId;
    }
    public String getAddress() {
        return address;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getWorkType() {
        return workType;
    }
}
