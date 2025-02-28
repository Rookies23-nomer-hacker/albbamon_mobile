package com.example.albbamon.dto.request;

import com.google.gson.annotations.SerializedName;

public class ChangePwRequestDto {

    @SerializedName("passwd")
    private String passwd;

    @SerializedName("newpasswd")
    private String newpasswd;

    public ChangePwRequestDto(String passwd, String newpasswd) {
        this.passwd = passwd;
        this.newpasswd = newpasswd;
    }

    public String getPasswd() { return passwd; }
    public String getNewpasswd() { return newpasswd; }
}
