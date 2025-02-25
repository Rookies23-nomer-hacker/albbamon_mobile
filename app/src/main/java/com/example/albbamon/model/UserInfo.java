package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("ceoNum")
    private String ceoNum;

    @SerializedName("company")
    private String company;

    @SerializedName("profileImg")
    private String profileImg;

    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCeoNum() { return ceoNum; }
    public String getCompany() { return company; }
    public String getProfileImg() { return profileImg; }
}
