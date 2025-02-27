package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class RecruitmentModel {
    @SerializedName("id")  // 채용 공고 ID
    private Long id;

    @SerializedName("userId")  // ✅ user_id 추가 (JSON 필드명과 매칭)
    private Long userId;

    @SerializedName("title")
    private String title;

    @SerializedName("wage")
    private Integer wage;

    @SerializedName("file")
    private String file;

    public Long getId() {
        return id;
    }

    public Long getUserId() {  // ✅ Getter 추가
        return userId;
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
}
