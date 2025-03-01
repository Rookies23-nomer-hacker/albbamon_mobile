package com.example.albbamon.Resume;

import android.util.Log;

import com.example.albbamon.dto.request.ResumeRequestDto;
import com.google.gson.Gson;

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

    // ✅ Setter 추가
    public void setSchool(String school) { this.school = school; }
    public void setStatus(String status) { this.status = status; }
    public void setPersonal(String personal) { this.personal = personal; }
    public void setWorkPlaceRegion(String workPlaceRegion) { this.workPlaceRegion = workPlaceRegion; }
    public void setWorkPlaceCity(String workPlaceCity) { this.workPlaceCity = workPlaceCity; }
    public void setIndustryOccupation(String industryOccupation) { this.industryOccupation = industryOccupation; }
    // ✅ 근무형태 저장 및 가져오기
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }
    public void setWorkingPeriod(String workingPeriod) { this.workingPeriod = workingPeriod; }
    public void setWorkingDay(String workingDay) { this.workingDay = workingDay; }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public void setPortfolio(List<String> portfolioFiles) {
        this.portfolioList.clear();
        for (String item : portfolioFiles) {
            if (!item.startsWith("[URL]")) { // ✅ URL 제외
                this.portfolioList.add(item.replace("[FILE] ", ""));
            }
        }
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

    // ✅ 포트폴리오 개수 반환
    public int getPortfolioCount() {
        return portfolioList.size();
    }

    public void setPortfolioData(String portfolioData) {
        this.portfolioData = portfolioData;
    }
    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }
    public void setResumeImgUrl(String resumeImgUrl) { this.resumeImgUrl = resumeImgUrl; }
    public void setResumeImgName(String resumeImgName) { this.resumeImgName = resumeImgName; }
    public void setResumeImgData(String resumeImgData) { this.resumeImgData = resumeImgData; }

    public ResumeRequestDto toResumeRequestDto() {
        Log.d("DEBUG-DM", "🚀 toResumeRequestDto() 호출됨");
        Log.d("DEBUG-DM", "📌 포트폴리오 파일 URL: " + portfolioUrl);
        Log.d("DEBUG-DM", "📌 포트폴리오 파일 이름: " + portfolioName);

        // ✅ LocalDateTime.now()를 ISO 8601 형식으로 변환

        ResumeRequestDto dto = new ResumeRequestDto(
                null, school, status, personal, workPlaceRegion, workPlaceCity, industryOccupation,
                employmentType, workingPeriod, workingDay, introduction,
                portfolioData != null ? portfolioData : "",
                portfolioUrl != null ? portfolioUrl : "",
                portfolioName != null ? portfolioName : "",
                resumeImgUrl != null ? resumeImgUrl : "",
                resumeImgName != null ? resumeImgName : "",
                resumeImgData != null ? resumeImgData : ""
        );

        Log.d("DEBUG-DM", "📌 변환된 ResumeRequestDto: " + new Gson().toJson(dto));
        return dto;
    }


}
