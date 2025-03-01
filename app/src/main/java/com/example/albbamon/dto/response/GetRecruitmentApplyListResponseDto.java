package com.example.albbamon.dto.response;

import com.example.albbamon.model.RecruitmentApply;
import java.util.List;

public class GetRecruitmentApplyListResponseDto {
    private List<RecruitmentApply> applyList; // 지원서 목록

    // 생성자
    public GetRecruitmentApplyListResponseDto(List<RecruitmentApply> applyList) {
        this.applyList = applyList;
    }

    // Getter
    public List<RecruitmentApply> getApplyList() {
        return applyList;
    }

    // Setter
    public void setApplyList(List<RecruitmentApply> applyList) {
        this.applyList = applyList;
    }
}
