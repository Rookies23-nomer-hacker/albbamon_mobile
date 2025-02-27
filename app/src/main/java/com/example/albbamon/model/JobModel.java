package com.example.albbamon.model;  // ✅ model 폴더에 맞게 변경


public class JobModel {
    private String title;
    private String location;
    private String salary;

    public JobModel(String title, String location, String salary) {
        this.title = title;
        this.location = location;
        this.salary = salary;
    }

    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getSalary() { return salary; }
}

