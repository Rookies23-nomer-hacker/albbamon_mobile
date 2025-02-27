package com.example.albbamon;

public class JobItem {
    private String title;
    private String company;
    private String workTime;
    private String location;
    private String salary;
    private String time;

    // 생성자
    public JobItem(String title, String company, String location, String salary, String time) {
        this.title = title;
        this.company = company;
        this.workTime = workTime;
        this.location = location;
        this.salary = salary;
        this.time = time;
    }

    // ✅ 올바른 Getter 메서드 추가
    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getWorkTime() {
        return workTime;  // 근무 시간 Getter 추가
    }


    public String getLocation() {
        return location;
    }

    public String getSalary() {
        return salary;
    }

    public String getTime() {
        return time;
    }
}
