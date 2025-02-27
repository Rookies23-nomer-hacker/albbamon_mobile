package com.example.albbamon.Resume;

import com.example.albbamon.dto.request.ResumeRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String portfolioData;
    private String portfolioUrl;
    private String portfolioName;
    private List<String> portfolioList;

    private String resumeImgUrl = null;  // ✅ null 가능하도록 초기값 설정
    private String resumeImgName = null;  // ✅ null 가능
    private String resumeImgData = null;  // ✅ null 가능

    private ResumeDataManager() {
        portfolioList = new ArrayList<>();
    }

    public static ResumeDataManager getInstance() {
        if (instance == null) {
            instance = new ResumeDataManager();
        }
        return instance;
    }

    // ✅ Getter 추가
    public Long getUserId() { return userId; }
    public String getSchool() { return school; }
    public String getStatus() { return status; }
    public String getPersonal() { return personal; }
    public String getWorkPlaceRegion() { return workPlaceRegion; }
    public String getWorkPlaceCity() { return workPlaceCity; }
    public String getIndustryOccupation() { return industryOccupation; }
    public String getEmploymentType() { return employmentType; }
    public String getWorkingPeriod() { return workingPeriod; }
    public String getWorkingDay() { return workingDay; }
    public String getIntroduction() { return introduction; }
    public String getPortfolioData() { return portfolioData; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public String getPortfolioName() { return portfolioName; }
    public List<String> getPortfolioList() { return portfolioList; }

    public String getResumeImgUrl() { return resumeImgUrl; }
    public String getResumeImgName() { return resumeImgName; }
    public String getResumeImgData() { return resumeImgData; }

    public void setResumeImgUrl(String resumeImgUrl) { this.resumeImgUrl = resumeImgUrl; }
    public void setResumeImgName(String resumeImgName) { this.resumeImgName = resumeImgName; }
    public void setResumeImgData(String resumeImgData) { this.resumeImgData = resumeImgData; }

    public ResumeRequestDto toResumeRequestDto() {
        return new ResumeRequestDto(
                userId, school, status, personal, workPlaceRegion, workPlaceCity, industryOccupation,
                employmentType, workingPeriod, workingDay, introduction, portfolioData, portfolioUrl, portfolioName,
                resumeImgUrl, resumeImgName, resumeImgData, // ✅ null 허용
                LocalDateTime.now(), LocalDateTime.now()
        );
    }
}
