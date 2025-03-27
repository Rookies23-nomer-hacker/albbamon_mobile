package com.example.albbamon.dto.request;

import com.google.gson.annotations.SerializedName;

public class RecruitmentApplyListRequestDto {

    @SerializedName("recruitmentId")
    private Long recruitmentId;

    public RecruitmentApplyListRequestDto(Long recruitmentId) {
        this.recruitmentId = recruitmentId;
    }

    public Long getRecruitmentId() { return recruitmentId; }
}
