package com.example.albbamon.model;

public class SupportCountsResponse {
    private String status;
    private String message;
    private Data data; // `Data` 객체 추가

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() { // `getData()` 메서드 추가
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data { // 내부 클래스로 `Data` 정의
        private int applyCount;

        public int getApplyCount() {
            return applyCount;
        }

        public void setApplyCount(int applyCount) {
            this.applyCount = applyCount;
        }
    }

    @Override
    public String toString() {
        return "SupportCountsResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
