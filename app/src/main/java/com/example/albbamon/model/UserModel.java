package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private UserData data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public UserData getData() { return data; }

    // 내부 클래스 추가 (data 필드 처리)
    public class UserData {
        @SerializedName("userInfo")
        private UserInfo userInfo;

        public UserInfo getUserInfo() { return userInfo; }
    }

    // userInfo 필드 정의
    public class UserInfo {
        @SerializedName("id")
        private long id;

        @SerializedName("name")
        private String name;

        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;

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
        public String getPassword() { return password; }
        public String getPhone() { return phone; }
        public String getCeoNum() { return ceoNum; }
        public String getCompany() { return company; }
        public String getProfileImg() { return profileImg; }


    }




}


