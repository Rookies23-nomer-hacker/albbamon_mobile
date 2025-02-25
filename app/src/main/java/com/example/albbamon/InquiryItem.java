package com.example.albbamon;

public class InquiryItem {
    private String category;
    private String title;
    private String date;
    private String status;

    public InquiryItem(String category, String title, String date, String status) {
        this.category = category;
        this.title = title;
        this.date = date;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}

