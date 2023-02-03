package com.example.myandroidpro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonData {

    @GET("/login")
    Call<List<UserModel>> getUsers(@Query("email") String user_email);
    @GET("/user_id")
    Call<List<UserModel>> getUserById(@Query("id") String user_id);

    @GET("posts")
    Call<List<PostModel>> getPosts();
    @FormUrlEncoded
    @POST("posts")
    Call<PostModel> createPost(
            @Field("title")String ed_title,
            @Field("body")String ed_post,
            @Field("author")String author
    );
    @FormUrlEncoded
    @POST("like")
    Call<PostModel> updateLike(
            @Field("_id")String _id,
            @Field("likes")int like
    );
    @FormUrlEncoded
    @POST("update-liked")
    Call<UserModel> updateLiked(
            @Field("user_id")String user_id,
            @Field("id")String post_id
    );
    @DELETE("/delete/{id}")
    Call<Void> deletePost(@Path("id")String post_id);

    @GET("/comment")
    Call<List<CommentModel>> getComments(@Query("post_id")String post_id);

    @GET("/comment_count")
    Call<Integer> getComment_count(@Query("post_id")String post_id);

    @FormUrlEncoded
    @POST("/comment")
    Call<List<CommentModel>> addComment(
            @Field("user_id") String user_id,
            @Field("post_id") String post_id,
            @Field("content") String content
    );


    @GET("/search-post")
    Call<List<PostModel>> searchPost(@Query("title")String title);

    @GET("/search-job")
    Call<List<JobModel>> searchJob(@Query("title")String title);

    @GET("job")
    Call<List<JobModel>> getJobs();
    @FormUrlEncoded
    @POST("job-post")
    Call<JobModel> createJobPost(
            @Field("poster_id")String poster_id,
            @Field("title")String job_title,
            @Field("description")String job_description,
            @Field("requirements")String job_requirements,
            @Field("salary")String job_salary,
            @Field("location")String job_location
    );
    @DELETE("/delete-job/{job-id}")
    Call<Void> deleteJobPost(@Path("job-id") String job_post_id);

    @POST("user")
    Call<UserModel> createUser(@Body UserModel user);
    @FormUrlEncoded
    @POST("register")
    Call<UserModel> createUser(
            @Field("user_name") String user_name,
            @Field("email") String user_email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/update-profile/{id}")
    Call<UserModel> updateUser(@Path("id")String id,
                               @Field("user_name") String user_name,
                               @Field("user_email")String user_email,
                               @Field("newpassword")String newpassword
    );

    @FormUrlEncoded
    @POST("/liked")
    Call<UserModel> userLikes(@Field("id")String id,
                              @Field("user_email")String user_email);


}
