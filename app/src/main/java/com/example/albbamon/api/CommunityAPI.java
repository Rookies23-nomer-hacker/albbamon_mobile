package com.example.albbamon.api;

import com.example.albbamon.dto.request.CreatePostRequestDto;
import com.example.albbamon.model.CommunityModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface CommunityAPI {
    // 게시글 리스트 가져오기
    @GET("/api/post/list")
    Call<PostListResponse> getAllPosts(
            @Query("size") int size,
            @Query("page") int page
    );
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
    @Multipart
    @POST("/api/post/write")
    Call<Void> createPost(
            @Part MultipartBody.Part file,   // 파일 업로드 (선택 가능)
            @Part("userId") RequestBody userId,
            @Part("title") RequestBody title,
            @Part("contents") RequestBody contents
    );

    //글 수정
    @POST("/api/post/update/{postId}")
    Call<Void> updatePost(
            @Path("postId") Long postId,
            @Body CreatePostRequestDto requestDto
    );

    //글 삭제
    @DELETE("/api/mobile/post/delete/{postId}")
    Call<Void> mobiledeletePost(
            @Path("postId") long postId,
            @Query("userId") long userId

    );
}
