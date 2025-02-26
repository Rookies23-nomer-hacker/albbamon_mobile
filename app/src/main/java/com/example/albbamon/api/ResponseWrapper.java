package com.example.albbamon.api;

import com.google.gson.annotations.SerializedName;

public class ResponseWrapper<T> {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data; // `data` 필드에 제네릭 타입을 사용

    // Getter 추가
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
