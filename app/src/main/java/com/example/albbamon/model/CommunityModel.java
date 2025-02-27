package com.example.albbamon.model;
import com.google.gson.annotations.SerializedName;


public class CommunityModel {
    @SerializedName("postId")
    private long postId;
    @SerializedName("userId")
    private long userId;
    @SerializedName("title")
    private String title;
    @SerializedName("contents")
    private String contents;
    @SerializedName("file")
    private String file_name;
    @SerializedName("createDate")
    private String createDate;
    @SerializedName("userName")
    private String userName;

    public long getPostId() { return postId; }
    public long getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContents() { return contents; }
    public String getFile_name() { return file_name; }
    public String getCreateDate() { return createDate; }
    public String getUserName() { return userName; }
}
