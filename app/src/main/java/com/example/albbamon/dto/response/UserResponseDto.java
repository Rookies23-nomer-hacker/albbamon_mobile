package com.example.albbamon.dto.response;

import com.google.gson.annotations.SerializedName;

public class UserResponseDto {
    @SerializedName("userId")
    private long userId;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("ceoNum")
    private String ceoNum;

    @SerializedName("company")
    private String company;

    @SerializedName("item")
    private String item;

    // ✅ 기본 생성자
    public UserResponseDto() {}

    // ✅ Getter & Setter 추가
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCeoNum() {
        return ceoNum;
    }

    public void setCeoNum(String ceoNum) {
        this.ceoNum = ceoNum;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}