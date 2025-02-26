package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class UserFindIdModel {
    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("ceoNum")
    private String ceoNum;

    @SerializedName("sucess")
    private boolean success;

    public String getEmail() {
        return email;
    }

    public String getCeoNum() { return ceoNum; }
    public String getPhone() {
        return phone;
    }

    public boolean isSuccess() {
        return success;
    }
}
