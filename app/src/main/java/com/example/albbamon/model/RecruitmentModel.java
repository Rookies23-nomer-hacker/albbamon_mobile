package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class RecruitmentModel implements Serializable {
    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String title;

    @SerializedName("wage")
    private Integer wage;

    @SerializedName("file")
    private String file;

    @SerializedName("item")
    private String item;

    @SerializedName("company")
    private String company;

    @SerializedName("contents") // ✅ 추가: 상세 내용 필드
    private String contents;

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

    public String getItem() {
        return item;
    }

    public String getCompany() {
        return company;
    }

    public String getContents() { // ✅ Getter 추가
        return contents;
    }
}
