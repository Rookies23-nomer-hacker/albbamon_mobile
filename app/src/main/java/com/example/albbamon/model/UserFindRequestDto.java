package com.example.albbamon.model;

public class UserFindRequestDto {
    private String name;
    private String phone;  // 개인회원 검색 시 사용
    private String ceoNum; // 기업회원 검색 시 사용

    // 기본 생성자
    public UserFindRequestDto() {}

    // Getter & Setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCeoNum() {
        return ceoNum;
    }
    public void setCeoNum(String ceoNum) {
        this.ceoNum = ceoNum;
    }
}
