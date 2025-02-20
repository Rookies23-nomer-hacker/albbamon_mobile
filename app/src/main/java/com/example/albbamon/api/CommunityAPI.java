package com.example.albbamon.api;

import com.example.albbamon.model.CommunityModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CommunityAPI {
    // 게시글 리스트 가져오기
    @GET("/api/post/")
    Call<List<CommunityModel>> getPosts();

    // 특정 게시글 가져오기
    @GET("/api/post/{postId}")
    Call<CommunityModel> getPostById(@Path("id") int postId);
}
