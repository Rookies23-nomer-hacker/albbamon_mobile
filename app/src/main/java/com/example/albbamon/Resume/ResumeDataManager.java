package com.example.albbamon.Resume;

import com.example.albbamon.model.ResumeRequestDto;

import java.time.LocalDateTime;

public class ResumeDataManager {
    private static ResumeDataManager instance;

    // 사용자 입력 데이터
    private Long userId;
    private String school;
    private String status;
    private String personal;
    private String workPlaceRegion;
    private String workPlaceCity;
    private String industryOccupation;
    private String employmentType;
    private String workingPeriod;
    private String workingDay;
    private String introduction;
    private String portfolioUrl;
    private String portfolioName;

    private ResumeDataManager() {}

    public static ResumeDataManager getInstance() {
        if (instance == null) {
            instance = new ResumeDataManager();
        }
        return instance;
    }

    // 데이터 저장 메서드
    public void setPersonalInfo(Long userId, String school, String status, String personal) {
        this.userId = userId;
        this.school = school;
        this.status = status;
        this.personal = personal;
    }

    public void setWorkInfo(String workPlaceRegion, String workPlaceCity, String industryOccupation, String employmentType) {
        this.workPlaceRegion = workPlaceRegion;
        this.workPlaceCity = workPlaceCity;
        this.industryOccupation = industryOccupation;
        this.employmentType = employmentType;
    }

    public void setWorkingConditions(String workingPeriod, String workingDay) {
        this.workingPeriod = workingPeriod;
        this.workingDay = workingDay;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setPortfolio(String portfolioUrl, String portfolioName) {
        this.portfolioUrl = portfolioUrl;
        this.portfolioName = portfolioName;
    }

    // 최종 이력서 데이터 반환
    public ResumeRequestDto toResumeRequestDto() {
        return new ResumeRequestDto(userId, school, status, personal, workPlaceRegion, workPlaceCity, industryOccupation,
                employmentType, workingPeriod, workingDay, introduction, portfolioUrl, portfolioName,
                LocalDateTime.now(), LocalDateTime.now());
    }
}
