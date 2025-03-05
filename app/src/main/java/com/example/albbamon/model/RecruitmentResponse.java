package com.example.albbamon.model;

import java.util.List;

public class RecruitmentResponse {
    private int status;
    private String message;
    private RecruitmentData data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public RecruitmentData getData() {
        return data;
    }

    public static class RecruitmentData {
        private List<RecruitmentModel> recruitmentList;
        private PageInfo pageInfo; // ✅ pageInfo 추가

        public List<RecruitmentModel> getRecruitmentList() {
            return recruitmentList;
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }
    }

    // ✅ pageInfo 클래스 추가
    public static class PageInfo {
        private int pageNum;
        private int pageSize;
        private int totalElements;
        private int totalPages;

        public int getPageNum() {
            return pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }
}
