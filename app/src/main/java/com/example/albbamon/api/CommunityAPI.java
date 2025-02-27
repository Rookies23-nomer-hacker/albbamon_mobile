package com.example.albbamon.api;

import com.example.albbamon.dto.request.CreatePostRequestDto;
import com.example.albbamon.model.CommunityModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface CommunityAPI {
    // 게시글 리스트 가져오기
    @GET("/api/post/list")
    Call<List<CommunityModel>> getPosts();
        //getAllPosts
    // 특정 게시글 가져오기
    @GET("/api/post/{postId}")
    Call<ResponseWrapper<CommunityModel>> getPostById(@Path("postId") long postId);

    //검색
    @GET("/api/post/search")
    Call<List<CommunityModel>> getSearchlist(@Query("keyword") String keyword);

    //글 작성
    @POST("/api/post/write")  // API 엔드포인트
    Call<Void> createPost(@Body CreatePostRequestDto postRequest);

    //글 수정
    @POST("/api/post/update/{postId}")
    Call<Void> updatePost(
            @Path("postId") Long postId,
            @Body CreatePostRequestDto requestDto
    );
}
