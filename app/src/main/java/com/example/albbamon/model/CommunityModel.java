package com.example.albbamon.model;
import com.google.gson.annotations.SerializedName;
public class CommunityModel {
    @SerializedName("postId")
    private int postId;
    @SerializedName("userId")
    private String userId;
    @SerializedName("title")
    private String title;
    @SerializedName("contents")
    private String contents;
    @SerializedName("file")
    private String file_name;
    @SerializedName("createDate")
    private String createDate;

    public int getPostId() { return postId; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContents() { return contents; }
    public String getFile_name() { return file_name; }
    public String getCreateDate() { return createDate; }
}
