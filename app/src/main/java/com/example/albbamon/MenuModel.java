package com.example.albbamon;

public class MenuModel {
    private String title;
    private int iconResId;

    public MenuModel(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}
