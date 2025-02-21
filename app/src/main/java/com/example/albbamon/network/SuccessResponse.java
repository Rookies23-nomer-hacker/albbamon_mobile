package com.example.albbamon.network;

public class SuccessResponse<T> {
    private int status;
    private String message;
    private T data;

    // Getter 메서드들
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}

