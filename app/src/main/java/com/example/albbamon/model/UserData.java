package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("userInfo")
    private UserInfo userInfo;

    public UserInfo getUserInfo() { return userInfo; }
}
