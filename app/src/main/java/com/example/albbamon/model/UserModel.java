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
}

