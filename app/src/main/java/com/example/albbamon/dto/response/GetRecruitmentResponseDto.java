package com.example.albbamon.dto.response;

import android.util.Log;

import com.example.albbamon.model.MyRecruitment;

import java.util.List;

public class GetRecruitmentResponseDto {
    private List<MyRecruitment> recruitmentList;

    public List<MyRecruitment> getRecruitmentList()  {
        return recruitmentList;
    }
}