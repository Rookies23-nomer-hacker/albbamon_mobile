package com.example.albbamon.model;

public class RecruitmentApplyRequest {
    private long userId;
    private long resumeId;

    public RecruitmentApplyRequest(long userId, long resumeId) {
        this.userId = userId;
        this.resumeId = resumeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getResumeId() {
        return resumeId;
    }

    public void setResumeId(long resumeId) {
        this.resumeId = resumeId;
    }
}
