package com.example.albbamon.dto.request;
import com.google.gson.annotations.SerializedName;


public class UpdateApplyStatusRequestDto {
    private String statusAsEnum;
    private String status;

    public UpdateApplyStatusRequestDto(String statusAsEnum, String status) {
        this.statusAsEnum = statusAsEnum;
        this.status = status;
    }

    public String getStatusAsEnum() {
        return statusAsEnum;
    }

    public String getStatus() {
        return status;
    }
}
