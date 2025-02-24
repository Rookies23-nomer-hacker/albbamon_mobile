package com.example.albbamon.model;

public class CommunityItem {
    private String title;
    private String content;
    private String user;
    private String date;

    public CommunityItem(String title, String content, String user, String date) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }
}
