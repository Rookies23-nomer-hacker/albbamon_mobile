package com.example.albbamon.dto.response;

import com.example.albbamon.model.UserInfo;
import com.google.gson.annotations.SerializedName;

public class GetUserInfoResponseDto {

    @SerializedName("userInfo")  // ✅ JSON에서 "userInfo" 필드와 정확히 매핑
    private UserInfo userInfo;  // <-- 변경됨

    public UserInfo getUserInfo() { return userInfo; }
}
