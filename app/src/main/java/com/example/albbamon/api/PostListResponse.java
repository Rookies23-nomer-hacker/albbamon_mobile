package com.example.albbamon.api;

import com.example.albbamon.dto.request.PostData;
import com.example.albbamon.model.CommunityModel;
import com.google.gson.annotations.SerializedName;

public class PostListResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private PostData data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public PostData getData() { return data; }
}
