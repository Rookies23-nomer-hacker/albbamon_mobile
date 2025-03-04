package com.example.albbamon.dto.request;

import com.example.albbamon.model.CommunityModel;
import com.example.albbamon.model.PageInfo;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PostData {
    @SerializedName("postList")
    private List<CommunityModel> postList;

    @SerializedName("pageInfo")
    private PageInfo pageInfo;

    public List<CommunityModel> getPostList() { return postList; }
    public PageInfo getPageInfo() { return pageInfo; }
}
