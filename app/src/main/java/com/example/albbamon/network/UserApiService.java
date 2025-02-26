package com.example.albbamon.network;

import com.example.albbamon.model.UserFindIdModel;
import com.example.albbamon.model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.POST;
import retrofit2.http.Body;

public interface UserApiService {
    @GET("/api/user/withdraw")
    Call<SuccessResponse> deleteUser(@Query("userId") long userId);

   @GET("/api/user/find-id")
   Call<List<UserFindIdModel>> findUserId(
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("ceoNum") String ceoNum);

    //Call<List<UserFindIdModel>> findBizUserId(String , String , String );
}



