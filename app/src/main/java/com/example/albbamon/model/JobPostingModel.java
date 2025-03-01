package com.example.albbamon.model;

import java.time.LocalDateTime;

public class JobPostingModel {
    private String title;
    private String contents;
    private String dueDate;
    private Integer wage;

    public JobPostingModel(String title, String contents, String dueDate, Integer wage){
        this.title = title;
        this.contents = contents;
        this.dueDate = dueDate;
        this.wage = wage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getWage() {
        return wage;
    }

    public void setWage(Integer wage) {
        this.wage = wage;
    }







}
