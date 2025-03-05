package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class PageInfo {
    @SerializedName("pageNum")
    private int pageNum;

    @SerializedName("pageSize")
    private int pageSize;

    @SerializedName("totalElements")
    private int totalElements;

    @SerializedName("totalPages")
    private int totalPages;

    public int getPageNum() { return pageNum; }
    public int getPageSize() { return pageSize; }
    public int getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
}
