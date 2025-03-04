package com.example.albbamon.model;

public class JobItemModel {
    private String date;
    private String applyType;
    private String company;
    private String jobTitle;
    private String deadline;

    public JobItemModel(String date, String applyType, String company, String jobTitle, String deadline) {
        this.date = date;
        this.applyType = applyType;
        this.company = company;
        this.jobTitle = jobTitle;
        this.deadline = deadline;
    }

    public String getDate() {
        return date;
    }

    public String getApplyType() {
        return applyType;
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getDeadline() {
        return deadline;
    }
}
