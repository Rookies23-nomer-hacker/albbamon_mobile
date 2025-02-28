package com.example.albbamon.model;

public class ResumeModel {
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

    public String getCreateDate() {
        return createDate;
    }

    public String getCareer() {
        return career;
    }

    public String getAddress() {
        return address;
    }

    public String getWorkType() {
        return workType;
    }
}
