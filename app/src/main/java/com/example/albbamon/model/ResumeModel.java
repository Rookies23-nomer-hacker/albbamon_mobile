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

    public long getResumeId() {
        return resumeId;
    }

    public void setResumeId(long resumeId) {
        this.resumeId = resumeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
