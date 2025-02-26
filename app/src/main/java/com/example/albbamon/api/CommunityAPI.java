package com.example.albbamon.api;

import com.example.albbamon.model.CommunityModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommunityAPI {
    // 게시글 리스트 가져오기
    @GET("/api/post/list")
    Call<List<CommunityModel>> getPosts();
        //getAllPosts
    // 특정 게시글 가져오기
    @GET("/api/post/{postId}")
    Call<ResponseWrapper<CommunityModel>> getPostById(@Path("postId") int postId);

    @GET("/api/post/search")
    Call<List<CommunityModel>> getSearchlist(@Query("keyword") String keyword);
}

