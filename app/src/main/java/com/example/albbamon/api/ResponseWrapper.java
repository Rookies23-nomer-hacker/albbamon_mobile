package com.example.albbamon.api;

import com.example.albbamon.model.ApplyStatusModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseWrapper<T> {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data; // `data` 필드에 제네릭 타입을 사용

    // Getter 추가
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // data 객체 안에 applyList
    public class ApplyData {
        private List<ApplyStatusModel> applyList;  // applyList 배열

        public List<ApplyStatusModel> getApplyList() {
            return applyList;
        }

        public void setApplyList(List<ApplyStatusModel> applyList) {
            this.applyList = applyList;
        }
    }
}
