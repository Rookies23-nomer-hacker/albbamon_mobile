package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class ResumeUserModel {

    @SerializedName("id")
    private int id;

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

    // ✅ JSON이 "data.userInfo" 안에 있는 경우 추가
    @SerializedName("userInfo")
    private ResumeUserModel userInfo;

    @SerializedName("data")
    private ResumeUserModel data;

    // ✅ 올바른 데이터를 가져오기 위한 Getter 수정
    public int getId() { return (data != null && data.userInfo != null) ? data.userInfo.id : id; }
    public String getName() { return (data != null && data.userInfo != null) ? data.userInfo.name : name; }
    public String getEmail() { return (data != null && data.userInfo != null) ? data.userInfo.email : email; }
    public String getPhone() { return (data != null && data.userInfo != null) ? data.userInfo.phone : phone; }
    public String getCeoNum() { return (data != null && data.userInfo != null) ? data.userInfo.ceoNum : ceoNum; }
    public String getCompany() { return (data != null && data.userInfo != null) ? data.userInfo.company : company; }
    public String getProfileImg() { return (data != null && data.userInfo != null) ? data.userInfo.profileImg : profileImg; }
}
