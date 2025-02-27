package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class RecruitmentModel {
    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String title;

    @SerializedName("wage")
    private Integer wage;

    @SerializedName("file")
    private String file;

    @SerializedName("item") // ✅ item 필드 추가
    private String item;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getWage() {
        return wage;
    }

    public String getFile() {
        return file;
    }

    public String getItem() { // ✅ Getter 추가
        return item;
    }
}
