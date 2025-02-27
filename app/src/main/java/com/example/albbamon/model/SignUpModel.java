package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class SignUpModel {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("phone")
    private String phone;

    @SerializedName("name")
    private String name;

    @SerializedName("ceoNum") // 기업 회원용 (일반 회원가입일 경우 빈 값)
    private String ceoNum;

    @SerializedName("company") // 기업 회원용 (일반 회원가입일 경우 빈 값)
    private String company;

    @SerializedName("isCompany") // 일반 회원(false) / 기업 회원(true) 구분
    private boolean isCompany;

    // 기본 생성자 (Gson 변환용)
    public SignUpModel() {}

    // 일반 회원가입용 생성자
    public SignUpModel(String email, String password, String phone, String name) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.ceoNum = ""; // 일반 회원가입일 경우 빈 값 처리
        this.company = "";
        this.isCompany = false; // 기본값: 일반 회원
    }

    // 기업 회원가입용 생성자
    public SignUpModel(String email, String password, String phone, String name, String ceoNum, String company) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.ceoNum = (ceoNum == null || ceoNum.trim().isEmpty()) ? "" : ceoNum;
        this.company = (company == null || company.trim().isEmpty()) ? "" : company;
        this.isCompany = true; // 기업 회원
    }

    // Getter 및 Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCeoNum() { return ceoNum; }
    public void setCeoNum(String ceoNum) { this.ceoNum = (ceoNum == null || ceoNum.trim().isEmpty()) ? "" : ceoNum; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = (company == null || company.trim().isEmpty()) ? "" : company; }

    public boolean isCompany() { return isCompany; }
    public void setCompany(boolean isCompany) { this.isCompany = isCompany; }
}
