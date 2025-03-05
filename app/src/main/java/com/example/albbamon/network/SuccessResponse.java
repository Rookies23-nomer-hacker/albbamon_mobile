package com.example.albbamon.network;

import com.google.gson.annotations.SerializedName;

public class SuccessResponse<T> {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")  // ✅ JSON에서 "data" 필드와 정확히 매핑
    private T data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
