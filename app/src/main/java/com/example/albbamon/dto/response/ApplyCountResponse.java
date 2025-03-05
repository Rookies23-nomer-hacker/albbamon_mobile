package com.example.albbamon.dto.response;

package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplyCountResponse {
    public String getStatus() {
        return status;
    }

    public void getStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private List<com.example.albbamon.model.ApplyItem> list;   // ✅ 추가된 부분: 지원 리스트

    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;

    private ApplyCountResponse(String status, String message, String data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // ✅ ★ 여기가 중요! `getList()` 메서드를 추가해야 함
    public List<com.example.albbamon.model.ApplyItem> getList() {
        return (list != null) ? list : List.of();
    }
}

//    private String status; // 응답 상태 (예: "success" 또는 "error")
//    private String message; // 응답 메시지 (예: "요청 성공" 또는 "에러 메시지")
//    private int count;  // 지원 횟수
//    private Object data; // 기타 데이터 (JSON 객체 등)
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//
//    public Object getData() {
//        return data;
//    }
//
//    public void setData(Object data) {
//        this.data = data;
//    }
//
//    @Override
//    public String toString() {
//        return "ApplyCountResponse{" +
//                "status='" + status + '\'' +
//                ", message='" + message + '\'' +
//                ", count=" + count +
//                ", data=" + data +
//                '}';
//    }

