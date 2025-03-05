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

    @SerializedName("item")
    private String item;

    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;

    @SerializedName("pwChkNum")
    private int pwChkNum;

    @SerializedName("pwCheck")
    private boolean pwCheck;

    // ✅ 기본 생성자 추가 (Gson이 필요함)
    public UserInfo() {}

    // ✅ Getter 추가
    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCeoNum() { return ceoNum; }
    public String getCompany() { return company; }
    public String getItem() { return item; }
    public String getLastModifiedDate() { return lastModifiedDate; }
    public int getPwChkNum() { return pwChkNum; }
    public boolean isPwCheck() { return pwCheck; }

    // ✅ toString() 추가하여 디버깅 가능하게 만듦
    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", ceoNum='" + ceoNum + '\'' +
                ", company='" + company + '\'' +
                ", item='" + item + '\'' +
                ", lastModifiedDate='" + lastModifiedDate + '\'' +
                ", pwChkNum=" + pwChkNum +
                ", pwCheck=" + pwCheck +
                '}';
    }
}
