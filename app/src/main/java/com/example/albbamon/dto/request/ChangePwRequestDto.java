package com.example.albbamon.dto.request;

import com.google.gson.annotations.SerializedName;

public class ChangePwRequestDto {
    @SerializedName("userId")
    private Long userId;

    @SerializedName("passwd")
    private String passwd;

    @SerializedName("newpasswd")
    private String newpasswd;

    public ChangePwRequestDto(Long userId, String passwd, String newpasswd) {
        this.userId = userId;
        this.passwd = passwd;
        this.newpasswd = newpasswd;
    }

    public Long getUserId() { return userId; }
    public String getPasswd() { return passwd; }
    public String getNewpasswd() { return newpasswd; }
}
