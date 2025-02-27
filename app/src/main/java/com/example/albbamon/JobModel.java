package com.example.albbamon;



public class JobModel {
    private String title;
    private String salary;
    private int imageResId;

    // 생성자
    public JobModel(String title, String salary, int imageResId) {
        this.title = title;
        this.salary = salary;
        this.imageResId = imageResId;
    }

    // Getter 메서드들
    public String getTitle() {
        return title;
    }

    public String getSalary() {
        return salary;
    }

    public int getImageResId() {
        return imageResId;
    }
}
